package org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.field;

import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.text.Text;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type.TypeIdentifier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * staticフィールド定義一覧
 */
public class StaticFieldDeclarations {

    List<StaticFieldDeclaration> list;

    public StaticFieldDeclarations(List<StaticFieldDeclaration> list) {
        this.list = list;
    }

    public List<StaticFieldDeclaration> list() {
        return list;
    }

    public String toNameText() {
        return Text.of(list, StaticFieldDeclaration::nameText);
    }

    public StaticFieldDeclarations selfDefineOnly() {
        return list.stream()
                .filter(StaticFieldDeclaration::isSelfDefine)
                .collect(Collectors.collectingAndThen(Collectors.toList(), StaticFieldDeclarations::new));
    }

    public List<TypeIdentifier> listTypeIdentifiers() {
        return list.stream()
                .map(staticFieldDeclaration -> staticFieldDeclaration.typeIdentifier())
                .collect(Collectors.toList());
    }
}
