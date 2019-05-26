package ru.fink.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class TripletService {

    public static final String ENCODING = "Windows-1251";
    @Getter
    private final Map<TripletRequestDto, TripletResponseDto> tripletRequests = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        String students = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("students.csv")).getFile();
        fillByFile(students);

        String superVisor = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("super-visor.csv")).getFile();
        fillByFile(superVisor);

        String superVisorAdditional = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("super-visor-additional.csv")).getFile();
        fillByFile(superVisorAdditional);

        String receipt = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("receipt.csv")).getFile();
        fillByFile(receipt);
    }

    private void fillByFile(String file) throws IOException {
        if (file == null) {
            log.warn("student file is not found");
            return;
        }

        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(file), ENCODING))
                .withCSVParser(csvParser)
                .build();

        String[] header = reader.readNext();
        String[] line;
        while ((line = reader.readNext()) != null) {
            for (int i = 1; i < line.length; i++) {
                tripletRequests.put(new TripletRequestDto(line[0], header[i]),
                        new TripletResponseDto(line[i]));
            }
        }
    }

}