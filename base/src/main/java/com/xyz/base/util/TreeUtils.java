package com.xyz.base.util;

import com.xyz.base.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

    public static <T extends TreeNode> List<T> convertList2Tree(List<T> treeNodeList) {
        if (treeNodeList == null || treeNodeList.size() == 0 || treeNodeList.size() == 1) {
            return treeNodeList;
        }

        List<T> convertedList = getChildren(treeNodeList, null);

        return convertedList;
    }

    private static <T extends TreeNode> List<T> getChildren(List<T> treeNodeList, Long parentId) {
        List<T> retList = new ArrayList<>();
        for (T t : treeNodeList) {
            if ((parentId == null && t.getParentId() == null)
                || (parentId != null && parentId.equals(t.getParentId()))) {
                retList.add(t);
                t.setChildren(getChildren(treeNodeList, t.getId()));
            }
        }

        return retList;
    }

}
