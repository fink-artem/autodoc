package ru.fink.utils;

import org.apache.commons.lang3.StringUtils;
import ru.fink.model.TreeNode;

public class Utils {

    /**
     * Возвращает true если деревья совпадают.
     * Если какой-то узел пустой, то считаем, что он совпадает с любым узлом
     */
    public static TreeNode matchTemplate(TreeNode candidate, TreeNode template) {
        TreeNode answer = new TreeNode();

        if (template.getNode().speechPart != null && template.getNode().speechPart != candidate.getNode().speechPart){
            return null;
        }
        answer.getNode().speechPart = candidate.getNode().speechPart;

        if (StringUtils.isNotEmpty(template.getNode().name)
                && !template.getNode().name.equals(candidate.getNode().name)){
            return null;
        }
        answer.getNode().name = candidate.getNode().name;

        answer.getNode().knowledgeSign = template.getNode().knowledgeSign;

        boolean flag;

        for (TreeNode templateTreeNode: template.getTreeNodes()) {

            flag = false;
            for (TreeNode treeNode: candidate.getTreeNodes()) {
                TreeNode matchTemplate = matchTemplate(treeNode, templateTreeNode);
                if (matchTemplate != null) {
                    answer.getTreeNodes().add(matchTemplate);
                    flag = true;
                }
            }

            if (!flag) {
                return null;
            }

        }

        return answer;
    }
}
