package org.dddjava.jig.domain.model.jigmodel.jigtype.member;

import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.method.MethodDeclaration;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.method.MethodDeclarations;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type.TypeIdentifier;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * メソッド一覧
 */
public class JigMethods {
    List<JigMethod> list;

    public JigMethods(List<JigMethod> list) {
        this.list = list;
    }

    public MethodDeclarations declarations() {
        return list.stream().map(JigMethod::declaration).collect(MethodDeclarations.collector());
    }

    public List<JigMethod> list() {
        list.sort(Comparator.comparing(method -> method.declaration().asFullNameText()));
        return list;
    }

    public JigMethod get(MethodDeclaration methodDeclaration) {
        for (JigMethod method : list) {
            if (method.declaration().sameIdentifier(methodDeclaration)) {
                return method;
            }
        }
        throw new NoSuchElementException(methodDeclaration.asFullNameText());
    }

    public boolean empty() {
        return list.isEmpty();
    }

    public List<TypeIdentifier> listUsingTypes() {
        return list.stream()
                .flatMap(jigMethod -> jigMethod.usingTypes().list().stream())
                .collect(Collectors.toList());
    }
}
