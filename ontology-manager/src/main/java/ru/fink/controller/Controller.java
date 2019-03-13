package ru.fink.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.fink.dto.ClassRequestDto;
import ru.fink.dto.ClassResponseDto;
import ru.fink.dto.TripletRequestDto;
import ru.fink.dto.TripletResponseDto;
import ru.fink.service.MainService;

import java.io.IOException;

@RestController
@RequestMapping(value = "/ontology")
@AllArgsConstructor
public class Controller {

    private final MainService mainService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody byte[] bytes) throws IOException {
        mainService.add(bytes);
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header("content-disposition", "attachment; " +
                        "filename=\"document.docx\"")
                .body(null);
    }

    @RequestMapping(value = "/get-object", method = RequestMethod.POST)
    public ClassResponseDto getObject(@RequestBody ClassRequestDto classRequestDto) {
        return mainService.getObject(classRequestDto);
    }

    @RequestMapping(value = "/resolve-triplet", method = RequestMethod.POST)
    public TripletResponseDto resolveTriplet(@RequestBody TripletRequestDto tripletRequestDto) {
        return mainService.resolveTriplet(tripletRequestDto);
    }

}
