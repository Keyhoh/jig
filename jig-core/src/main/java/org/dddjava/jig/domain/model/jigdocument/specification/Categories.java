package org.dddjava.jig.domain.model.jigdocument.specification;

import org.dddjava.jig.domain.model.jigdocument.documentformat.DocumentName;
import org.dddjava.jig.domain.model.jigdocument.documentformat.JigDocument;
import org.dddjava.jig.domain.model.jigdocument.stationery.*;
import org.dddjava.jig.domain.model.jigmodel.categories.CategoryAngle;
import org.dddjava.jig.domain.model.jigmodel.categories.CategoryType;
import org.dddjava.jig.domain.model.jigmodel.categories.CategoryTypes;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.field.StaticFieldDeclaration;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.class_.ClassRelations;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

/**
 * 区分の切り口一覧
 */
public class Categories {

    List<CategoryAngle> list;

    Categories(List<CategoryAngle> list) {
        this.list = list;
    }

    public static Categories create(CategoryTypes categoryTypes, ClassRelations classRelations) {
        List<CategoryAngle> list = new ArrayList<>();
        for (CategoryType categoryType : categoryTypes.list()) {
            list.add(new CategoryAngle(categoryType, classRelations));
        }
        return new Categories(list);
    }

    public List<CategoryAngle> list() {
        return list.stream()
                .sorted(Comparator.comparing(categoryAngle -> categoryAngle.typeIdentifier()))
                .collect(toList());
    }

    public DiagramSources valuesDotText(JigDocumentContext jigDocumentContext) {
        if (list.isEmpty()) {
            return DiagramSource.empty();
        }

        Map<TypeIdentifier, CategoryAngle> map = list.stream()
                .collect(toMap(CategoryAngle::typeIdentifier, Function.identity()));

        PackageStructure packageStructure = PackageStructure.from(new ArrayList<>(map.keySet()));

        String structureText = packageStructure.toDotText(
                typeIdentifier -> {
                    CategoryAngle categoryAngle = map.get(typeIdentifier);
                    String values = categoryAngle.constantsDeclarations().list().stream()
                            .map(StaticFieldDeclaration::nameText)
                            .collect(joining("</td></tr><tr><td border=\"1\">", "<tr><td border=\"1\">", "</td></tr>"));
                    String categoryName = categoryAngle.nodeLabel("<br/>");

                    if (categoryAngle.hasBehaviour()) {
                        return Node.typeOf(typeIdentifier)
                                .html("<table border=\"0\" cellspacing=\"0\"><tr><td>" + categoryName + "</td></tr>" + values + "</table>");
                    } else {
                        return Node.typeOf(typeIdentifier)
                                .weakColor()
                                .html("<table border=\"0\" cellspacing=\"0\"><tr><td>" + categoryName + "</td></tr>" + values + "</table>");
                    }
                }
        );

        DocumentName documentName = jigDocumentContext.documentName(JigDocument.CategoryDiagram);
        return DiagramSource.createDiagramSource(
                documentName, new StringJoiner("\n", "graph \"" + documentName.label() + "\" {", "}")
                        .add("label=\"" + documentName.label() + "\";")
                        .add("layout=fdp;")
                        .add("rankdir=LR;")
                        .add(Node.DEFAULT)
                        .add(structureText)
                        .toString());
    }
}
