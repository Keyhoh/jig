package org.dddjava.jig.application.service;

import org.dddjava.jig.domain.model.configuration.ConfigurationContext;
import org.dddjava.jig.domain.model.architecture.BusinessRuleCondition;
import org.dddjava.jig.domain.model.declaration.namespace.PackageDepth;
import org.dddjava.jig.domain.model.declaration.namespace.PackageIdentifier;
import org.dddjava.jig.domain.model.implementation.bytecode.TypeByteCodes;
import org.dddjava.jig.domain.model.networks.packages.PackageNetwork;
import org.dddjava.jig.infrastructure.DefaultLayout;
import org.dddjava.jig.infrastructure.LocalProject;
import org.dddjava.jig.infrastructure.configuration.Configuration;
import org.dddjava.jig.infrastructure.configuration.JigProperties;
import org.dddjava.jig.infrastructure.configuration.OutputOmitPrefix;
import org.junit.jupiter.api.Test;
import testing.TestSupport;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyServiceTest {

    @Test
    void パッケージ依存() {

        LocalProject localProject = configuration().localProject();
        ImplementationService implementationService = configuration().implementationService();

        DependencyService sut = configuration().dependencyService();


        TypeByteCodes typeByteCodes = implementationService.readProjectData(localProject);
        PackageNetwork packageNetwork = sut.packageDependencies(typeByteCodes);

        // パッケージのリストアップ
        List<String> packageNames = packageNetwork.allPackages().stream()
                .map(packageIdentifier -> packageIdentifier.format(value -> value))
                .collect(Collectors.toList());
        assertThat(packageNames)
                .containsExactlyInAnyOrder(
                        "stub.domain.model",
                        "stub.domain.model.booleans",
                        "stub.domain.model.category",
                        "stub.domain.model.relation",
                        "stub.domain.model.relation.clz",
                        "stub.domain.model.relation.method",
                        "stub.domain.model.relation.field",
                        "stub.domain.model.relation.annotation",
                        "stub.domain.model.relation.enumeration",
                        "stub.domain.model.type",
                        "stub.domain.model.type.fuga",
                        "stub.domain.model.relation.constant.to_primitive_constant",
                        "stub.domain.model.relation.constant.to_primitive_wrapper_constant"
                );

        // パッケージの関連
        assertThat(packageNetwork.packageDependencies().list())
                .extracting(dependency -> {
                    PackageIdentifier from = dependency.from();
                    PackageIdentifier to = dependency.to();
                    return from.format(value -> value) + " -> " + to.format(value -> value);
                })
                .containsExactlyInAnyOrder(
                        "stub.domain.model -> stub.domain.model.relation.annotation",
                        "stub.domain.model.relation -> stub.domain.model.relation.clz",
                        "stub.domain.model.relation -> stub.domain.model.relation.field",
                        "stub.domain.model.relation -> stub.domain.model.relation.method",
                        "stub.domain.model.relation -> stub.domain.model.relation.enumeration",
                        "stub.domain.model.relation -> stub.domain.model.relation.constant.to_primitive_wrapper_constant"
                );
    }

    Configuration configuration() {
        Path path = Paths.get(TestSupport.defaultPackageClassURI());
        return new Configuration(
                new DefaultLayout(
                        path.toString(),
                        path.toString(),
                        // Mapper.xmlのためだが、ここではHitしなくてもテストのクラスパスから読めてしまう
                        "not/read/resources",
                        // TODO ソースディレクトリの安定した取得方法が欲しい
                        "not/read/sources"
                ),
                new JigProperties(
                        new BusinessRuleCondition("stub.domain.model.+"),
                        new OutputOmitPrefix(),
                        new PackageDepth(),
                        false
                ),
                new ConfigurationContext() {
                    @Override
                    public String classFileDetectionWarningMessage() {
                        return "";
                    }

                    @Override
                    public String modelDetectionWarningMessage() {
                        return "";
                    }
                });
    }

}
