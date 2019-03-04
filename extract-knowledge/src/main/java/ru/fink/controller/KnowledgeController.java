package ru.fink.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fink.service.KnowledgeService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/knowledge")
@AllArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addKnowledge(@RequestBody byte[] byteArray) throws IOException {
        knowledgeService.extractKnowledge(byteArray);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
