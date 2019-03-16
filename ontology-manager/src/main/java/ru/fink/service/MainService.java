package ru.fink.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MainService {

    private File tempFile;

    private static final Map<String, List<String>> classRequests = new HashMap<>();
    private static final Map<TripletRequestDto, TripletResponseDto> tripletRequests = new HashMap<>();

    static {
        classRequests.put("студент", Arrays.asList("Финк Артём Альбертович"));
        classRequests.put("тип_квалификационной_работы", Arrays.asList("выпускная квалификационная работа"));

        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "тема_дипломной"),
                new TripletResponseDto("Разработка методов"));
        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "факультет"),
                new TripletResponseDto("ФИТ"));
        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "направление_подготовки"),
                new TripletResponseDto("информатика и вычислительная техника"));
        tripletRequests.put(new TripletRequestDto("Пальчунов Дмитрий Евгеньевич", "звание"),
                new TripletResponseDto("д.ф.м.н"));
        tripletRequests.put(new TripletRequestDto("Пальчунов Дмитрий Евгеньевич", "должность"),
                new TripletResponseDto("зав. кафедрой"));
        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "степень"),
                new TripletResponseDto("магистр"));
        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "кафедра"),
                new TripletResponseDto("КОИ"));
        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "группа"),
                new TripletResponseDto("17228"));
        tripletRequests.put(new TripletRequestDto("Финк Артём Альбертович", "научный_руководитель"),
                new TripletResponseDto("Пальчунов Дмитрий Евгеньевич"));
    }

    public void add(byte[] bytes) throws IOException {
        tempFile = File.createTempFile("ontology", ".owl");
        FileUtils.writeByteArrayToFile(tempFile, bytes);
    }

    public ClassResponseDto getObject(ClassRequestDto classRequestDto) {
        Map<String, List<String>> collect = classRequestDto.getRequest().stream()
                .collect(Collectors.toMap(Function.identity(), classRequests::get));
        return new ClassResponseDto(collect);
    }

    public TripletResponseDto resolveTriplet(TripletRequestDto tripletRequestDto) {
        return tripletRequests.get(tripletRequestDto);
    }
}