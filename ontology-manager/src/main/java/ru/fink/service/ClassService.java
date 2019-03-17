package ru.fink.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassService {

    @Getter
    private final Map<String, List<String>> classRequests = new HashMap<>();

    @PostConstruct
    public void init() {
        classRequests.put("студент", Arrays.asList("Финк Артём Альбертович", "Капустина Алина Игоревна"));
        classRequests.put("тип_квалификационной_работы", Arrays.asList("выпускная квалификационная работа"));
    }

}