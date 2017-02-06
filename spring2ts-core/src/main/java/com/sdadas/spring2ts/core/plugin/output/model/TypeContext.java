package com.sdadas.spring2ts.core.plugin.output.model;

import com.google.common.collect.Sets;
import com.sdadas.spring2ts.core.typescript.types.TypeName;

import java.util.Set;

/**
 * @author Sławomir Dadas
 */
public class TypeContext {

    private TypeName parent;

    private Set<String> contextualTypes;

    private String packageName;

    public TypeContext(TypeName parent, String packageName) {
        this.parent = parent;
        this.packageName = packageName;
        this.contextualTypes = Sets.newHashSet(parent.getGenerics());
    }

    public TypeName getParent() {
        return parent;
    }

    public void setParent(TypeName parent) {
        this.parent = parent;
    }

    public Set<String> getContextualTypes() {
        return contextualTypes;
    }

    public void setContextualTypes(Set<String> contextualTypes) {
        this.contextualTypes = contextualTypes;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
