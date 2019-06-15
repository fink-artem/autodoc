package ru.fink.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fink.service.TemplateService;

@CrossOrigin
@RestController
@RequestMapping(value = "/start")
@AllArgsConstructor
public class TemplateController {

    private final TemplateService templateService;
    private byte[] documents;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity getDocument(@RequestBody byte[] bytes) {
        documents = templateService.generate(bytes);

        if (documents == null) {
            return ResponseEntity.badRequest().body("ERROR");
        } else {
            return ResponseEntity.ok().body("OK");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/download")
    public ResponseEntity downloadDocument() {
        return ResponseEntity
                .ok()
                .contentLength(documents.length)
                .contentType(MediaType.TEXT_PLAIN)
                .header("content-disposition", "attachment; " +
                        "filename=\"document.zip\"")
                .body(documents);
    }
}
