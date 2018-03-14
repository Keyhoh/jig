package jig.classlist;

import jig.classlist.report.Report;
import jig.classlist.report.ReportFormat;
import jig.classlist.report.ReportPerspective;
import jig.classlist.report.ReportService;
import jig.domain.model.tag.Tag;
import jig.infrastructure.asm.AsmExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "jig")
public class ClassListApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClassListApplication.class, args);

        context.getBean(ClassListApplication.class).output();
    }

    @Value("${target.class}")
    String targetClasses;
    @Value("${output.list.name}")
    String outputPath;
    @Value("${output.list.type}")
    String listType;

    @Autowired
    AsmExecutor asmExecutor;
    @Autowired
    ReportService reportService;

    public void output() {
        Path[] paths = Arrays.stream(targetClasses.split(":"))
                .map(Paths::get)
                .toArray(Path[]::new);
        asmExecutor.load(paths);

        Tag tag = Tag.valueOf(listType.toUpperCase());

        ReportPerspective perspective = ReportPerspective.from(tag);
        Report report = perspective.getReport(tag, reportService);

        ReportFormat.from(outputPath)
                .writer()
                .writeTo(report, Paths.get(outputPath));
    }
}

