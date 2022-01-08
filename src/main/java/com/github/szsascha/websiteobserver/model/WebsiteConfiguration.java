package com.github.szsascha.websiteobserver.model;

import lombok.Data;

import java.util.List;

@Data
public class WebsiteConfiguration {

    private String url;

    private long interval;

    private List<String> ignoreExpressions;

}
