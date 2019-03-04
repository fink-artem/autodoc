package ru.fink.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@PropertySource(value = "classpath:/download.properties", encoding = "UTF-8")
@Component
public class DownloadConfig {

    @Value("${download.url}")
    private String url;

    @Value("${download.dir}")
    private String dir;

}
