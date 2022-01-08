package com.github.szsascha.websiteobserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Website {

    private String url;

    private String content;

    private long lastCheck;

}
