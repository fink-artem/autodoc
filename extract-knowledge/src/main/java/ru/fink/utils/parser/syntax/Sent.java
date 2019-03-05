package ru.fink.utils.parser.syntax;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Sent {

    private List<Rel> rels = new ArrayList<>();
}
