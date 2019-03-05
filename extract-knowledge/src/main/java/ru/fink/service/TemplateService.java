package ru.fink.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.fink.model.SyntaxTree;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {

    @Getter
    private List<SyntaxTree> templates = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        URL directory = TemplateService.class.getClassLoader().getResource("templates");
        if (directory == null) {
            throw new RuntimeException("Templates directory not found");
        }

        File[] files = new File(directory.getPath()).listFiles();
        if (files == null) {
            throw new RuntimeException("Templates directory not found");
        }

        for (File file : files) {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new FileInputStream(file);
            templates.add(mapper.readValue(is, SyntaxTree.class));
        }
    }

}
