package ru.fink.converter;

import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Converter {

    public static ClassRequestDto convertKeysToClassRequestDto(Set<String> keys) {
        return new ClassRequestDto(keys);
    }

    public static Map<String, Object> convertResponseToMap(ClassResponseDto response) {
        if (response == null) {
            return Collections.emptyMap();
        }
        return response.getResponse();
    }
}
