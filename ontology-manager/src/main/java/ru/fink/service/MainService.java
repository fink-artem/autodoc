package ru.fink.service;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainService {

    private final ClassService classService;
    private final TripletService tripletService;

    public void add(byte[] bytes) throws IOException {
        File tempFile = File.createTempFile("ontology", ".owl");
        FileUtils.writeByteArrayToFile(tempFile, bytes);
    }

    public ClassResponseDto getObject(ClassRequestDto classRequestDto) {
        Map<String, List<String>> classRequests = classService.getClassRequests();
        Map<String, List<String>> collect = classRequestDto.getRequest().stream()
                .collect(Collectors.toMap(Function.identity(), classRequests::get));
        return new ClassResponseDto(collect);
    }

    public TripletResponseDto resolveTriplet(TripletRequestDto tripletRequestDto) {
        return tripletService.getTripletRequests().get(tripletRequestDto);
    }
}