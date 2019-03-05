package ru.fink.model;

import lombok.Getter;
import ru.fink.utils.parser.seman.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TreeNode {

    /**
     * Информация о слове
     */
    protected final Node node = new Node();

    /**
     * Информация о потомках
     */
    protected final List<TreeNode> treeNodes = new ArrayList<>();

}
