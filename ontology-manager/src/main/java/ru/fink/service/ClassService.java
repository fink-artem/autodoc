package ru.fink.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static final String ENCODING = "Windows-1251";

    @Getter
    private final Map<String, List<String>> classRequests = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        classRequests.put("студент", extractStudents());
        classRequests.put("тип_квалификационной_работы", Arrays.asList("выпускная квалификационная работа"));
        classRequests.put("компетенция_ок_4_1", Arrays.asList("Способность сформулировать цели и задачи, " +
                "определять требования к результату исследования"));
        classRequests.put("компетенция_ок_4_2", Arrays.asList("Применять обоснованные методы в своих исследованиях"));
        classRequests.put("компетенция_пк_2_2", Arrays.asList("Уметь применять методы анализа и обработки полученных" +
                " данных при проведении научных исследований в рамках поставленных задач"));
        classRequests.put("компетенция_ок_9_1", Arrays.asList("Структурировать описание полученных результатов"));
        classRequests.put("компетенция_ок_9_2", Arrays.asList("Оформлять результаты исследований в виде статей " +
                "и докладов на научно-технических конференциях в соответствии с установленными требованиями"));
        classRequests.put("компетенция_опк_6_1",Arrays.asList("Анализировать научные публикации других авторов " +
                "по теме выпускной квалификационной работы"));
        classRequests.put("компетенция_опк_6_2", Arrays.asList("Вырабатывать критерии оценки научных " +
                "результатов, исходя из специфики решаемой задачи "));
        classRequests.put("компетенция_пк_7_1", Arrays.asList("Уметь критически анализировать альтернативные " +
                "варианты решения научных и технических, фундаментальных и прикладных задач"));
        classRequests.put("компетенция_пк_7_2", Arrays.asList("Определять эффективные методы решения" +
                " поставленных задач и применять их на практике"));
    }

    private List<String> extractStudents() throws IOException {
        String file = Objects.requireNonNull(getClass().getClassLoader()
                .getResource("students.csv")).getFile();

        if (file == null) {
            log.warn("student file is not found");
            return Collections.emptyList();
        }

        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(file), ENCODING))
                .withCSVParser(csvParser)
                .build();

        reader.readNext();
        String[] line;
        List<String> studentsFio = new ArrayList<>();
        while ((line = reader.readNext()) != null) {
            studentsFio.add(line[0]);
        }
        return studentsFio;
    }

}