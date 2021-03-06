package org.dddjava.jig.presentation.view.handler;

import org.dddjava.jig.domain.model.jigdocument.documentformat.JigDocument;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class HandleResult {

    JigDocument jigDocument;
    List<Path> outputFilePaths;
    String failureMessage;

    HandleResult(JigDocument jigDocument, List<Path> outputFilePaths, String failureMessage) {
        this.jigDocument = jigDocument;
        this.outputFilePaths = outputFilePaths;
        this.failureMessage = failureMessage;
    }

    public HandleResult(JigDocument jigDocument, List<Path> outputFilePaths) {
        this(jigDocument, outputFilePaths, outputFilePaths.isEmpty() ? "skip" : null);
    }

    public HandleResult(JigDocument jigDocument, String failureMessage) {
        this(jigDocument, Collections.emptyList(), failureMessage);
    }

    public String outputFilePathsText() {
        return outputFilePaths.stream()
                .map(Path::toAbsolutePath)
                .map(Path::normalize)
                .map(Path::toString)
                .collect(joining(", ", "[ ", " ]"));
    }

    public boolean success() {
        return failureMessage == null;
    }

    public JigDocument jigDocument() {
        return jigDocument;
    }
}
