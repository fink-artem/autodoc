package ru.fink.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ClassService {

    @Getter
    private final Map<String, List<String>> classRequests = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        classRequests.put("студент", extractStudents());
        classRequests.put("тип_квалификационной_работы", Arrays.asList("выпускная квалификационная работа"));
    }

    private List<String> extractStudents() throws IOException {
        String file = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("students.csv")).getFile();

        if (file == null) {
            log.warn("student file is not found");
            return Collections.emptyList();
        }

        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(csvParser).build();

        reader.readNext();
        String[] line;
        List<String> studentsFio = new ArrayList<>();
        while ((line = reader.readNext()) != null) {
            studentsFio.add(line[0]);
        }
        return studentsFio;
    }

}