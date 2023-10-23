package com.example.JewelerProgressReport.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "settings")
public class SettingProperties {
    private String timeZone;
}
