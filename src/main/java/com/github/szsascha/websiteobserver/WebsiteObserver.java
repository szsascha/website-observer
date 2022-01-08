package com.github.szsascha.websiteobserver;

import com.github.szsascha.websiteobserver.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@SpringBootApplication
public class WebsiteObserver {

    @Autowired
    private Observer observer;

    public static void main(String[] args) {
        SpringApplication.run(WebsiteObserver.class);
    }

    @Scheduled(fixedRateString = "${observer.rate}")
    public void observe() {
        observer.observe();
    }

}
