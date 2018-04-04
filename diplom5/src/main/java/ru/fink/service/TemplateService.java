package ru.fink.service;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.StringWriter;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class TemplateService {

    private VelocityEngine velocityEngine;

    @PostConstruct
    public void init() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("input.encoding", UTF_8.toString());
        velocityEngine.setProperty("output.encoding", UTF_8.toString());
        velocityEngine.init();
    }

    public String generate(String filePath, Map<String, String> map) {
        Template template = velocityEngine.getTemplate(filePath);
        VelocityContext context = new VelocityContext(map);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return String.valueOf(writer);
    }


}
