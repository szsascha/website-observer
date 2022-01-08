package com.github.szsascha.websiteobserver.observer;

import com.github.szsascha.websiteobserver.model.Website;
import com.github.szsascha.websiteobserver.model.WebsiteConfiguration;

import com.github.szsascha.websiteobserver.notification.NotificationService;
import com.github.szsascha.websiteobserver.storage.WebsiteRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "websites")
@Slf4j
@Service
public class Observer {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebsiteRepository repository;

    @Setter
    private List<WebsiteConfiguration> configuration;

    private final HttpClient httpClient;

    public Observer() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void observe() {
        configuration.forEach(this::observeWebsite);
    }

    private void observeWebsite(WebsiteConfiguration websiteConfiguration) {
        final Website existingWebsite = repository.findByUrl(websiteConfiguration.getUrl()).orElse(null);
        if (existingWebsite != null &&
                System.currentTimeMillis() - existingWebsite.getLastCheck() >= websiteConfiguration.getInterval()) {
            log.debug("Don't check {} because configured interval is {} and last check was {}. ({})",
                    existingWebsite.getUrl(),
                    websiteConfiguration.getInterval(),
                    existingWebsite.getLastCheck(),
                    System.currentTimeMillis() - existingWebsite.getLastCheck());
            return;
        }

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(websiteConfiguration.getUrl()))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(bodyString -> {
                    final Website website = buildWebsite(websiteConfiguration, bodyString);
                    repository.put(website);

                    if (existingWebsite == null) {
                        log.info("Add new website {}.", website.getUrl());
                        log.debug("Content: {}", website.getContent());
                        return;
                    }

                    log.info("Checking {} for changes.", website.getUrl());
                    log.debug("Content: {}", website.getContent());

                    if (!existingWebsite.getContent().equals(website.getContent())) {
                        log.info("Website {} changed. Sending notification...", website.getUrl());
                        notificationService.notifyWithMessage("Website changed! " + website.getUrl());
                    } else {
                        log.info("No changes found on {}.", website.getUrl());
                    }
                });
    }

    private Website buildWebsite(WebsiteConfiguration configuration, String content) {
        if (configuration.getIgnoreExpressions() != null) {
            for (String ignoreExpression : configuration.getIgnoreExpressions()) {
                content = content.replaceAll(ignoreExpression, "");
            }
        }

        return Website.builder()
                .url(configuration.getUrl())
                .content(content)
                .lastCheck(System.currentTimeMillis())
                .build();
    }

}
