package org.dddjava.jig.domain.model.jigmodel.jigtype.class_;

import org.dddjava.jig.domain.model.jigmodel.lowmodel.alias.TypeAlias;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.annotation.Annotation;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.method.Visibility;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type.TypeIdentifier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 型の属性
 */
public class JigTypeAttribute {
    TypeAlias typeAlias;
    TypeKind typeKind;
    Visibility visibility;

    List<Annotation> annotations;

    public JigTypeAttribute(TypeAlias typeAlias, TypeKind typeKind, Visibility visibility, List<Annotation> annotations) {
        this.typeAlias = typeAlias;
        this.typeKind = typeKind;
        this.visibility = visibility;
        this.annotations = annotations;
    }

    public TypeAlias alias() {
        return typeAlias;
    }

    public TypeKind kind() {
        return typeKind;
    }

    public Visibility visibility() {
        return visibility;
    }

    List<TypeIdentifier> listUsingTypes() {
        // TODO アノテーションの属性に書かれる型が拾えていない
        return annotations.stream()
                .map(annotation -> annotation.typeIdentifier())
                .collect(Collectors.toList());
    }

    public String descriptionText() {
        return typeAlias.descriptionText();
    }
}
