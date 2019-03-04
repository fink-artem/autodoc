package ru.fink.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@PropertySource(value = "classpath:/ontology.properties", encoding = "UTF-8")
@Component
public class OntologyConfig {

    @Value("${ontology.repository.url}")
    private String ontologyRepositoryUrl;

}
