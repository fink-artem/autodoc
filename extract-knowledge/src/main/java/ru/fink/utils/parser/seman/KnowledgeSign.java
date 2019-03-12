package ru.fink.utils.parser.seman;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgeSign {

    /**
     * Объект или класс
     */
    private KnowledgeType type;

    /**
     * Класс, к которому относится объект
     * Может быть заполнено только для типа OBJECT
     */
    private String classObject;

    /**
     * Родительский класс
     * Может быть заполнено только для типа Class
     */
    private String parentClass;

    private String predicate;

    private String subject;

    private KnowledgeType subjectType;

}
