package ru.fink.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import ru.fink.parser.seman.Node;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TreeNode {

    /**
     * Информация о слове
     */
    private Node node;

    /**
     * Информация о потомках
     */
    private final List<TreeNode> treeNodes = new ArrayList<>();

    /**
     * Возвращает true если деревья совпадают.
     * Если какой-то узел пустой, то считаем, что он совпадает с любым узлом
     */
    public boolean equalsTemplates(TreeNode treeNode) {
        if (node.speechPart != treeNode.node.speechPart) {
            return false;
        }
        if (StringUtils.isNotEmpty(node.name) && StringUtils.isNotEmpty(treeNode.node.name)
                && !node.name.equals(treeNode.node.name)) {
            return false;
        }
        return true;
    }

}
