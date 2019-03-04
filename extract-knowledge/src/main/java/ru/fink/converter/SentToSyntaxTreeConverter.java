package ru.fink.converter;

import ru.fink.model.SyntaxTree;
import ru.fink.model.TreeNode;
import ru.fink.parser.seman.Link;
import ru.fink.parser.seman.Node;
import ru.fink.parser.seman.Sent;

import java.util.*;
import java.util.stream.Collectors;

public class SentToSyntaxTreeConverter {

    public static List<SyntaxTree> convert(Sent sent) {

        Map<Integer, List<Integer>> parents = new HashMap<>();

        sent.getLinkList().forEach(link -> {
            if (parents.containsKey(link.getSecondNodeNumber())) {
                parents.get(link.getSecondNodeNumber()).add(link.getFirstNodeNumber());
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(link.getFirstNodeNumber());
                parents.put(link.getSecondNodeNumber(), list);
            }
        });

        Set<Integer> childs = sent.getLinkList().stream()
                .map(Link::getFirstNodeNumber)
                .collect(Collectors.toSet());
        Set<Integer> roots = parents.keySet().stream()
                .filter(id -> !childs.contains(id))
                .collect(Collectors.toSet());

        return roots.stream().map(el -> {
            SyntaxTree syntaxTree = new SyntaxTree();

            TreeNode treeNode = buildTreeNode(el, sent.getNodes(), parents);

            syntaxTree.setRootNode(treeNode);

            return syntaxTree;
        }).collect(Collectors.toList());

    }

    private static TreeNode buildTreeNode(int index, Map<Integer, Node> nodes,
                                          Map<Integer, List<Integer>> parents) {

        TreeNode treeNode = new TreeNode();
        treeNode.setNode(nodes.get(index));

        if (parents.containsKey(index)) {
            List<TreeNode> collect = parents.get(index).stream()
                    .map(el -> buildTreeNode(el, nodes, parents))
                    .collect(Collectors.toList());

            treeNode.getTreeNodes().addAll(collect);
        }

        return treeNode;
    }
}
