package jig.domain.model.relation;

import jig.domain.model.thing.Name;

public enum RelationType {
    DEPENDENCY,
    FIELD,
    METHOD,
    METHOD_RETURN_TYPE,
    METHOD_PARAMETER,
    METHOD_USE_TYPE,
    IMPLEMENT,
    METHOD_USE_METHOD;

    public Relation of(Name from, Name to) {
        return new Relation(from, to, this);
    }
}