package org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type;

import java.util.List;
import java.util.stream.Collectors;

/**
 * パラメータ化された型一覧
 */
public class ParameterizedTypes {
    List<ParameterizedType> list;

    public ParameterizedTypes(List<ParameterizedType> list) {
        this.list = list;
    }

    List<TypeIdentifier> listTypeIdentifiers() {
        return list.stream()
                .flatMap(parameterizedType -> parameterizedType.listTypeIdentifiers().stream())
                .collect(Collectors.toList());
    }
}
