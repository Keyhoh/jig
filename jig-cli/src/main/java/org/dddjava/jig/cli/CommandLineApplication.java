package org.dddjava.jig.cli;

import org.dddjava.jig.application.service.ImplementationService;
import org.dddjava.jig.domain.basic.ClassFindFailException;
import org.dddjava.jig.domain.model.implementation.ProjectData;
import org.dddjava.jig.infrastructure.LocalProject;
import org.dddjava.jig.infrastructure.configuration.Configuration;
import org.dddjava.jig.presentation.view.handler.JigDocumentHandlers;
import org.dddjava.jig.presentation.view.report.JigDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.util.List;

@SpringBootApplication(scanBasePackages = "org.dddjava.jig")
public class CommandLineApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CommandLineApplication.class, args);
    }

    @Autowired
    CliConfig cliConfig;

    @Autowired
    ExtraScript extraScript;

    @Override
    public void run(String... args) {
        long startTime = System.currentTimeMillis();
        try {
            List<JigDocument> jigDocuments = cliConfig.jigDocuments();
            Configuration configuration = cliConfig.configuration();
            ImplementationService implementationService = configuration.importService();
            LocalProject localProject = configuration.localProject();
            JigDocumentHandlers jigDocumentHandlers = configuration.documentHandlers();

            LOGGER.info("プロジェクト情報の取り込みをはじめます");

            ProjectData projectData = implementationService.readProjectData(localProject);

            Path outputDirectory = cliConfig.outputDirectory();
            for (JigDocument jigDocument : jigDocuments) {
                jigDocumentHandlers.handle(jigDocument, projectData, outputDirectory);
            }

            extraScript.invoke(projectData);
        } catch (ClassFindFailException e) {
            LOGGER.warn(e.warning().textWithSpringEnvironment(cliConfig.environment));
        }
        LOGGER.info("合計時間: {} ms", System.currentTimeMillis() - startTime);
    }

    @Bean
    @ConditionalOnMissingBean
    ExtraScript extraScript() {
        return projectData -> {
            // 何もしない
        };
    }
}
