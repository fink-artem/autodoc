package ru.fink.model;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SyntaxTree {

    private TreeNode rootNode = new TreeNode();

    @Data
    public class TreeNode{
        private List<TreeNode> treeNodes = new ArrayList<>();
    }
}
