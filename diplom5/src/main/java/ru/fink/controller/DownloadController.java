package ru.fink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fink.service.OntologyService;
import ru.fink.service.TemplateService;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/download")
public class DownloadController {

    private final TemplateService templateService;
    private final OntologyService ontologyService;

    @Autowired
    public DownloadController(TemplateService templateService, OntologyService ontologyService) {
        this.templateService = templateService;
        this.ontologyService = ontologyService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getDocument() {
//        Map<String, String> map = ontologyService.getMap();
        String document = null;
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

}
