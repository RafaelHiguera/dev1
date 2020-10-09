package com.umontreal.visitor;

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
        try {
            int beginLine = cid.getBegin().get().line;
            scanner = new Scanner(this.file);
            MetricExtractor.skipLines(scanner, beginLine-2);
            int numberOfLines = MetricExtractor.class_LOC(scanner);
            scanner = new Scanner(this.file);

            MetricExtractor.skipLines(scanner, beginLine-2);
            int numberOfLinesWithComment = MetricExtractor.class_method_CLOC(scanner);

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