package ru.fink.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.fink.config.OntologyConfig;
import ru.fink.converter.Converter;
import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;

import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class OntologyService {

    private OntologyConfig configuration;
    private RestTemplate restTemplate;

    public Map<String, Object> getValuesByKey(Set<String> keys) {
//        return Collections.emptyMap();
//        return Collections.singletonMap("студент", "Артём");
        String url = configuration.getOntologyRepositoryUrl();
        ClassRequestDto request = Converter.convertKeysToClassRequestDto(keys);
        ResponseEntity<ClassResponseDto> ontologyResponseDtoResponseEntity =
                restTemplate.postForEntity(url, request, ClassResponseDto.class);
        ClassResponseDto response = ontologyResponseDtoResponseEntity.getBody();
        return Converter.convertResponseToMap(response);
    }

}
