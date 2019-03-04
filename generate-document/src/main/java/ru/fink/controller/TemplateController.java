package ru.fink.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fink.service.TemplateService;

import java.util.List;

@RestController
@RequestMapping(value = "/start")
@AllArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity getDocument(@RequestBody byte[] bytes) {
        List<byte[]> document = templateService.generate(bytes);
        return ResponseEntity
                .ok()
                .contentLength(document.size())
                .contentType(MediaType.TEXT_PLAIN)
                .header("content-disposition", "attachment; " +
                        "filename=\"document.docx\"")
                .body(document);
    }

}
