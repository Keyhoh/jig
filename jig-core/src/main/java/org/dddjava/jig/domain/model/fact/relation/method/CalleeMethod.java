package org.dddjava.jig.domain.model.fact.relation.method;

import org.dddjava.jig.domain.model.declaration.method.MethodDeclaration;

public class CalleeMethod {
    MethodDeclaration methodDeclaration;

    public CalleeMethod(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }
}
