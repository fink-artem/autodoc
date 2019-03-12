package ru.fink.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.fink.config.OntologyConfig;

@Service
@AllArgsConstructor
public class ClientOntologyService {

    private OntologyConfig configuration;
    private RestTemplate restTemplate;

    @Async
    public void sendOntology(byte[] bytes) {
        String url = configuration.getOntologyRepositoryUrl();
        restTemplate.postForEntity(url, bytes, String.class);
    }

}
