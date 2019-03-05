
package ru.fink.utils.parser.seman;

import lombok.Getter;
import lombok.Setter;
import ru.fink.utils.parser.syntax.SyntaxRel;

@Getter
@Setter
public class Link {
    
    private SemanRel semanType;
    private SyntaxRel synanType;
    private int firstNodeNumber;
    private int secondNodeNumber;
    
}
