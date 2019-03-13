package ru.fink.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import java.io.File;
import java.io.IOException;

@Service
public class MainService {

    private File tempFile;

    public void add(byte[] bytes) throws IOException {
        tempFile = File.createTempFile("ontology", ".owl");
        FileUtils.writeByteArrayToFile(tempFile, bytes);
    }

    public ClassResponseDto getObject(ClassRequestDto classRequestDto) {
        return null;
    }

    public TripletResponseDto resolveTriplet(TripletRequestDto tripletRequestDto) {
        return null;
    }
}