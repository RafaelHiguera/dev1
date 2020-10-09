package com.umontreal.visitor;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.umontreal.bean.ClassMetric;
import com.umontreal.bean.IMetric;
import com.umontreal.utils.MetricExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * The type ClassVisitor.
 * Visit all class in file create a classMetric object for each of them and store it in the collector.
 */
public class ClassVisitor extends VoidVisitorAdapter<List<ClassMetric>> {
    private final File file;

    public ClassVisitor(File file) {
        this.file = file;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, List<ClassMetric> collector) {
        super.visit(cid, collector);
        String path = this.file.getPath();
        String className = cid.getName().asString();

        // Find class String and pass it to MetricExtractor functions
        Scanner scanner;
        int beginLine = 0;
        if (cid.getBegin().isPresent()) {
            beginLine = cid.getBegin().get().line;
        }

        // Where to class start in the file, if its the main class start at the beginning of file
        int linesToSkip = 0;
        if (cid.isNestedType()) {
            linesToSkip = beginLine-1;
            if (cid.getComment().isPresent()) {
                Range commentRange =  cid.getComment().get().getRange().get();
                int commentCorrection = commentRange.end.line - commentRange.begin.line + 1;
                linesToSkip = linesToSkip - commentCorrection;
            }
        }

        try {
            // LOC
            scanner = new Scanner(this.file);
            MetricExtractor.skipLines(scanner, linesToSkip);
            int numberOfLines = MetricExtractor.class_LOC(scanner);

            // CLOC
            scanner = new Scanner(this.file);
            MetricExtractor.skipLines(scanner, linesToSkip);
            int numberOfLinesWithComment = MetricExtractor.class_method_CLOC(scanner);

            // WMC: Sum of CC for each method in class
            int wmc = 0;
            for (MethodDeclaration method: cid.getMethods()) {
                scanner = new Scanner(method.toString());
                wmc += MetricExtractor.ccMcCabe(scanner)[IMetric.methodInUseCC];
            }

            // Create and collect ClassMetric Object
            ClassMetric classMetric = new ClassMetric(path, className, numberOfLines, numberOfLinesWithComment, wmc);
            collector.add(classMetric);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}