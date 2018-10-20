package org.dddjava.jig.domain.model.unit.method;

/**
 * メソッドの可視性
 */
public enum Accessor {
    PUBLIC,
    NOT_PUBLIC;

    public boolean isPublic() {
        return this == PUBLIC;
    }

    public boolean isNotPublic() {
        return !isPublic();
    }
}