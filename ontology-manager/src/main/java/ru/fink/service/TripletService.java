package ru.fink.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TripletService {

    @Getter
    private final Map<TripletRequestDto, TripletResponseDto> tripletRequests = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        fillMasters();
        fillSuperVisor();
    }

    private void fillMasters() throws IOException {
        String file = getClass().getClassLoader().getResource("students.csv").getFile();

        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(csvParser).build();

        String[] line;
        while ((line = reader.readNext()) != null) {
            System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
        }

    }


    private void fillSuperVisor() {
        tripletRequests.put(new TripletRequestDto("Пальчунов Дмитрий Евгеньевич", "звание"),
                new TripletResponseDto("д.ф.-м.н."));
        tripletRequests.put(new TripletRequestDto("Пальчунов Дмитрий Евгеньевич", "должность"),
                new TripletResponseDto("заведующий кафедрой, КОИ ФИТ НГУ"));
    }

}