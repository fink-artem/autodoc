package ru.fink.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.fink.config.OntologyConfig;
import ru.fink.converter.Converter;
import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class ClientOntologyService {

    private OntologyConfig configuration;
    private RestTemplate restTemplate;

    public Map<String, Object> getObjectsByClasses(Set<String> keys) {
        String url = configuration.getOntologyRepositoryUrl();
        ClassRequestDto request = Converter.convertKeysToClassRequestDto(keys);
        ResponseEntity<ClassResponseDto> ontologyResponseDtoResponseEntity =
                restTemplate.postForEntity(url, request, ClassResponseDto.class);
        ClassResponseDto response = ontologyResponseDtoResponseEntity.getBody();
        return Converter.convertClassResponseToMap(response);
    }

    public String resolveTriplet(String subject, String predicat) {
        String url = configuration.getOntologyRepositoryUrl();
        TripletRequestDto request = Converter.convertKeyToTripletRequestDto(subject, predicat);
        ResponseEntity<TripletResponseDto> ontologyResponseDtoResponseEntity =
                restTemplate.postForEntity(url, request, TripletResponseDto.class);
        TripletResponseDto response = ontologyResponseDtoResponseEntity.getBody();
        return Converter.convertTripletResponseToMap(response);
    }
}
