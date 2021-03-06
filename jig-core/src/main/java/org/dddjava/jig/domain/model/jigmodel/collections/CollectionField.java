package org.dddjava.jig.domain.model.jigmodel.collections;

import org.dddjava.jig.domain.model.jigmodel.businessrules.BusinessRuleFields;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.field.FieldType;

public class CollectionField {
    BusinessRuleFields businessRuleFields;

    public CollectionField(BusinessRuleFields businessRuleFields) {
        this.businessRuleFields = businessRuleFields;
    }

    public FieldType fieldType() {
        return businessRuleFields.onlyOneFieldType();
    }
}
