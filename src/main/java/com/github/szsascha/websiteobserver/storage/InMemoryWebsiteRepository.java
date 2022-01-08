package com.github.szsascha.websiteobserver.storage;

import com.github.szsascha.websiteobserver.model.Website;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@Service
public class InMemoryWebsiteRepository implements WebsiteRepository {

    private Set<Website> websites = new HashSet<>();

    @Override
    public void put(Website website) {
        findByUrl(website.getUrl()).ifPresent(websites::remove);
        websites.add(website);
    }

    @Override
    public Optional<Website> findByUrl(String url) {
        return websites.stream()
                .filter(website -> website.getUrl().equalsIgnoreCase(url))
                .findAny();
    }

}
