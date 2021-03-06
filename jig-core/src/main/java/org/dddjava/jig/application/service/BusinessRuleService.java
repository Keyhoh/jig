package org.dddjava.jig.application.service;

import org.dddjava.jig.application.repository.JigSourceRepository;
import org.dddjava.jig.domain.model.jigdocument.implementation.CategoryUsageDiagram;
import org.dddjava.jig.domain.model.jigdocument.implementation.MethodSmellList;
import org.dddjava.jig.domain.model.jigdocument.specification.Categories;
import org.dddjava.jig.domain.model.jigmodel.businessrules.BusinessRules;
import org.dddjava.jig.domain.model.jigmodel.categories.CategoryTypes;
import org.dddjava.jig.domain.model.jigmodel.collections.CollectionAngles;
import org.dddjava.jig.domain.model.jigmodel.collections.CollectionTypes;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.annotation.ValidationAnnotatedMembers;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.class_.ClassRelations;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.method.MethodRelations;
import org.dddjava.jig.domain.model.jigmodel.services.ServiceMethods;
import org.dddjava.jig.domain.model.jigsource.jigloader.analyzed.Architecture;
import org.dddjava.jig.domain.model.jigsource.jigloader.analyzed.TypeFacts;
import org.springframework.stereotype.Service;

/**
 * ビジネスルールの分析サービス
 */
@Service
public class BusinessRuleService {

    final Architecture architecture;
    final JigSourceRepository jigSourceRepository;

    public BusinessRuleService(Architecture architecture, JigSourceRepository jigSourceRepository) {
        this.architecture = architecture;
        this.jigSourceRepository = jigSourceRepository;
    }

    /**
     * ビジネスルール一覧を取得する
     */
    public BusinessRules businessRules() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        return typeFacts.toBusinessRules(architecture);
    }

    /**
     * メソッドの不吉なにおい一覧を取得する
     */
    public MethodSmellList methodSmells() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        MethodRelations methodRelations = typeFacts.toMethodRelations();
        return new MethodSmellList(businessRules(), methodRelations);
    }

    /**
     * 区分一覧を取得する
     */
    public Categories categories() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        CategoryTypes categoryTypes = CategoryTypes.from(businessRules());
        ClassRelations classRelations = typeFacts.toClassRelations();

        return Categories.create(categoryTypes, classRelations);
    }

    /**
     * コレクションを分析する
     */
    public CollectionAngles collections() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        BusinessRules businessRules = businessRules();
        CollectionTypes collectionTypes = new CollectionTypes(businessRules);

        return new CollectionAngles(collectionTypes, typeFacts.toClassRelations());
    }

    /**
     * 区分使用図
     */
    public CategoryUsageDiagram categoryUsages() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        ServiceMethods serviceMethods = new ServiceMethods(typeFacts.applicationMethodsOf(architecture));

        return new CategoryUsageDiagram(serviceMethods, typeFacts.toBusinessRules(architecture));
    }

    public ValidationAnnotatedMembers validationAnnotatedMembers() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        return typeFacts.validationAnnotatedMembers();
    }
}
