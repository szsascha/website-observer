package com.github.szsascha.websiteobserver.storage;

import com.github.szsascha.websiteobserver.model.Website;

import java.util.Optional;

public interface WebsiteRepository {

    void put(Website website);

    Optional<Website> findByUrl(String url);

}
