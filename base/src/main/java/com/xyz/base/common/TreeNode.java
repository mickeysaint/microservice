package com.xyz.base.common;

import java.util.List;

public interface TreeNode<T> {
    Long getId();

    void setId(Long id);

    Long getParentId();

    void setParentId(Long parentId);

    List<T> getChildren();

    void setChildren(List<T> children);
}
