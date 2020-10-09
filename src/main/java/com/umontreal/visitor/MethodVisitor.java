package com.umontreal.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.umontreal.bean.MethodMetric;
import com.umontreal.utils.MetricExtractor;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * The type MethodVisitor.
 * Visit all method in file create a methodMetric object for each of them and store it in the collector.
 */
public class MethodVisitor extends VoidVisitorAdapter<List<MethodMetric>> {
    private final File file;

    public MethodVisitor(File file) { this.file = file; }

    @Override
    public void visit(MethodDeclaration md, List<MethodMetric> collector){
        super.visit(md, collector);

        String path = this.file.getPath();

        String className = "";
        ClassOrInterfaceDeclaration parentClass = getClass(md);
        if (parentClass != null) {
            className = parentClass.getName().asString();
        }
        String methodName = reformatMethodName(md.getDeclarationAsString(false, false, false));

        String methodImplementation = "";
        if (md.getComment().isPresent()) {
            methodImplementation = md.getComment().get().toString();
        }
        methodImplementation = methodImplementation + md.getDeclarationAsString();
        if (md.getBody().isPresent()) {
            methodImplementation = methodImplementation + md.getBody().get().toString();
        }

        // LOC
        Scanner scanner = new Scanner(methodImplementation);
        int numberOfLines = MetricExtractor.method_LOC(scanner);

        // CLOC
        scanner = new Scanner(methodImplementation);
        int numberOfLinesWithComment = MetricExtractor.class_method_CLOC(scanner);

        // CC
        scanner = new Scanner(methodImplementation);
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