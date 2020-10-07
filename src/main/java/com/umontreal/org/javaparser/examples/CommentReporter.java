package com.umontreal.org.javaparser.examples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

public class CommentReporter {

    private static final String FILE_PATH = "src/main/java/com/umontreal/org/javaparser/examples/ReversePolishNotation.java";

    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));

        List<CommentReportEntry> comments = cu.getAllContainedComments()
                .stream()
                .map(p -> new CommentReportEntry(p.getClass().getSimpleName(),
                        p.getContent(),
                        p.getRange().get().begin.line,
                        !p.getCommentedNode().isPresent()))
                .collect(Collectors.toList());

        comments.forEach(System.out::println);

//        System.out.println(cu.getAllContainedComments().get(3));
//        System.out.println(cu.getAllContainedComments().get(3).getClass().getSimpleName());
//        System.out.println(cu.getAllContainedComments().get(3).getContent().replace("\n", "").replace("\r", "").trim());
    }


    private static class CommentReportEntry {
        private String type;
        private String text;
        private int lineNumber;
        private boolean isOrphan;

        CommentReportEntry(String type, String text, int lineNumber, boolean isOrphan) {
            this.type = type;
            this.text = text;
            this.lineNumber = lineNumber;
            this.isOrphan = isOrphan;
        }

        @Override
        public String toString() {
            return lineNumber + "|" + type + "|" + isOrphan + "|" + text.replace("\n", "").replace("\r", "").trim();
        }
    }
}