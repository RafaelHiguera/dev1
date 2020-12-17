package com.umontreal;

import java.io.*;
import java.util.*;

import com.umontreal.bean.ClassMetric;
import com.umontreal.bean.MethodMetric;
import com.umontreal.utils.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.umontreal.visitor.ClassVisitor;
import com.umontreal.visitor.MethodVisitor;

/**
 * Main class which contain the entry point of the application.
 */
public class Main {

    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     * args[0] is the path of the folder
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Enter directory path as argument");
            return;
        }
        String path = args[0];
        File projectDir = new File(path);

        List<MethodMetric> methodMetricsList = new ArrayList<>();
        List<ClassMetric> classMetricList = new ArrayList<>();

        try {

            extractMetric(projectDir, methodMetricsList, classMetricList);
            CsvPrinter.printMetricsToCSV("classes.csv", "chemin,class,classe_LOC,classe_CLOC,classe_DC,WMC,classe_BC", classMetricList);
            CsvPrinter.printMetricsToCSV("methodes.csv", "chemin,class,methode,methode_LOC,methode_CLOC,methode_DC,CC,methode_BC", methodMetricsList);
            System.out.println("Extraction complete");
        } catch (FileNotFoundException exception) {
            System.out.println("The file " + projectDir.getPath() + " was not found.");
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

    /**
     * Extract metric from projectDir by filling the methodMetricsList and classMetricList.
     *
     * @param projectDir        the project directory
     * @param methodMetricsList a MethodMetrics list
     * @param classMetricList   a ClassMetric list
     * @throws FileNotFoundException the file not found exception
     */
    public static void extractMetric(File projectDir, List<MethodMetric> methodMetricsList, List<ClassMetric> classMetricList) throws FileNotFoundException {
        System.out.println("Starting metric extraction on " + projectDir.getName());
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
//            System.out.println("Parsing: " + file.getName());

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
