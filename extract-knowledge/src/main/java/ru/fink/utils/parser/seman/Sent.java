
package ru.fink.utils.parser.seman;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Sent {
    
    private Map<Integer, Node> nodes = new HashMap();
    private List<Link> linkList = new ArrayList<>();
}
