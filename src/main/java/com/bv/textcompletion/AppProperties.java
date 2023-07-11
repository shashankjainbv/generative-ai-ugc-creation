package com.bv.textcompletion;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("app")
public class AppProperties {
    private String profile;
    private String region;
    private String secretName;
    private String host;
    private String protocol;
    private Boolean isLocal;
}
