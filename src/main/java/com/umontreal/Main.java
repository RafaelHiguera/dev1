package com.umontreal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.umontreal.bean.ClassMetric;
import com.umontreal.bean.MethodMetric;
import com.umontreal.utils.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.umontreal.visitor.ClassVisitor;
import com.umontreal.visitor.MethodVisitor;

public class Main {

    public static void main(String[] args) {
        // Pass this in arguments for tests:
        // "test/org/javaparser/examples/"
        String path = args[0];
        File projectDir = new File(path);

        List<MethodMetric> methodMetricsList = new ArrayList<>();
        List<ClassMetric> classMetricList = new ArrayList<>();

        try {
            extractMetric(projectDir, methodMetricsList, classMetricList);
            CsvPrinter.printMetricsToCSV("classes.csv", "chemin,class,classe_LOC,classe_CLOC,classe_DC,WMC,classe_BC", classMetricList);
            CsvPrinter.printMetricsToCSV("methodes.csv", "chemin,class,methode,methode_LOC,methode_CLOC,methode_DC,CC,methode_BC", methodMetricsList);
        } catch (FileNotFoundException exception) {
            System.out.println("The file " + projectDir.getPath() + " was not found.");
        }
    }

    public static void extractMetric(File projectDir, List<MethodMetric> methodMetricsList, List<ClassMetric> classMetricList) throws FileNotFoundException {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            CompilationUnit cu = StaticJavaParser.parse(file);

            List<ClassMetric> classMetricListFromFile = new ArrayList<>();
            VoidVisitor<List<ClassMetric>> classVisitor = new ClassVisitor(file);
            classVisitor.visit(cu, classMetricListFromFile);
            classMetricList.addAll(classMetricListFromFile);

            List<MethodMetric> methodMetricsListFromFile = new ArrayList<>();
            VoidVisitor<List<MethodMetric>> methodVisitor = new MethodVisitor(file);
            methodVisitor.visit(cu, methodMetricsListFromFile);
            methodMetricsList.addAll(methodMetricsListFromFile);

        }).explore(projectDir);
    }
}
