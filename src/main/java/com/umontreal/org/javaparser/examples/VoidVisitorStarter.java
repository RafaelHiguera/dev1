package com.umontreal.org.javaparser.examples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class VoidVisitorStarter {

    private static final String FILE_PATH = "src/main/java/com/umontreal/org/javaparser/examples/ReversePolishNotation.java";

    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));
        // Test1
        VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);

        /*
        Test2
         */
        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, methodNames);
        methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));
    }

    /*
     * Test Multiple ligne
     */
    private static class MethodNamePrinter extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            System.out.println("Method Name Printed: " + md.getName());
        }
    }

    // TEST1
    private static class MethodNameCollector extends VoidVisitorAdapter<List<String>> {
        @Override
        //Hello
        public void visit(MethodDeclaration md, List<String> collector) {
            super.visit(md, collector);
            collector.add(md.getNameAsString());
        }
    }
}