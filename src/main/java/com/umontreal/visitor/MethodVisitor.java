package com.umontreal.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.umontreal.bean.MethodMetric;
import com.umontreal.utils.MetricExtractor;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MethodVisitor extends VoidVisitorAdapter<List<MethodMetric>> {
    private final File file;

    public MethodVisitor(File file) { this.file = file; }

    @Override
    public void visit(MethodDeclaration md, List<MethodMetric> collector){
        super.visit(md, collector);

        String path = this.file.getPath();
        String className = Objects.requireNonNull(getClass(md)).getName().asString();
        String methodName = reformatMethodName(md.getDeclarationAsString(false, false, false));

        Scanner scanner = new Scanner(md.getDeclarationAsString() + md.getBody().get().toString());
        int numberOfLines = MetricExtractor.method_LOC(scanner);
        scanner = new Scanner(md.getDeclarationAsString() + md.getBody().get().toString());
        int numberOfLinesWithComment = MetricExtractor.class_method_CLOC(scanner);
        scanner = new Scanner(md.getDeclarationAsString() + md.getBody().get().toString());
        int[] cc = MetricExtractor.ccMcCabe(scanner);

        // Create and collect MethodMetric Object
        MethodMetric methodMetric = new MethodMetric(path, className, methodName, numberOfLines, numberOfLinesWithComment, cc);
        collector.add(methodMetric);
    }

    private static String reformatMethodName(String methodDeclaration) {
        methodDeclaration = methodDeclaration.split(" ", 2)[1];
        methodDeclaration = methodDeclaration.replace("(", "_");
        methodDeclaration = methodDeclaration.replace(", ", "_");
        methodDeclaration = methodDeclaration.replace(")", "");
        return methodDeclaration;
    }

    private static ClassOrInterfaceDeclaration getClass(Node node) {
        while (!(node instanceof ClassOrInterfaceDeclaration)) {
            if(node.getParentNode().isPresent()) {
                node = node.getParentNode().get();
            } else return null;
        }
        return (ClassOrInterfaceDeclaration) node;
    }
}