package ru.fink.converter;

import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Converter {

    public static ClassRequestDto convertKeysToClassRequestDto(Set<String> keys) {
        return new ClassRequestDto(keys);
    }

    public static Map<String, Object> convertClassResponseToMap(ClassResponseDto response) {
        if (response == null) {
            return Collections.emptyMap();
        }
        return response.getResponse();
    }

    public static TripletRequestDto convertKeyToTripletRequestDto(String subject, String predicat) {
        return new TripletRequestDto(subject, predicat);
    }

    public static String convertTripletResponseToMap(TripletResponseDto response) {
        if (response == null) {
            return "";
        }
        return response.getObject();
    }
}
