package org.dddjava.jig.domain.model.jigdocument.specification;

import org.dddjava.jig.domain.model.jigmodel.architecture.ArchitectureComponents;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.package_.PackageIdentifier;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.declaration.type.TypeIdentifier;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.class_.ClassRelation;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.class_.ClassRelations;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.packages.PackageRelation;
import org.dddjava.jig.domain.model.jigmodel.lowmodel.relation.packages.PackageRelations;

import java.util.ArrayList;
import java.util.List;

/**
 * アーキテクチャー単位に丸めたパッケージ関連
 */
public class ArchitectureRelations {

    List<PackageRelation> list;

    public ArchitectureRelations(List<PackageRelation> list) {
        this.list = list;
    }

    public static ArchitectureRelations from(ArchitectureComponents architectureComponents, ClassRelations classRelations) {
        ArrayList<PackageRelation> list = new ArrayList<>();
        for (ClassRelation classRelation : classRelations.list()) {
            TypeIdentifier from = classRelation.from();
            TypeIdentifier to = classRelation.to();

            PackageIdentifier fromPackage = architectureComponents.packageIdentifier(from);

            if (to.isJavaLanguageType()) {
                // 興味のない関連
                continue;
            }
            PackageIdentifier toPackage = architectureComponents.packageIdentifier(to);

            if (fromPackage.equals(toPackage)) {
                // 自己参照
                continue;
            }
            PackageRelation e = new PackageRelation(fromPackage, toPackage);
            if (!list.contains(e)) {
                list.add(e);
            }
        }

        return new ArchitectureRelations(list);
    }

    public boolean worthless() {
        return list.isEmpty();
    }

    public PackageRelations packageRelations() {
        return new PackageRelations(list);
    }
}
