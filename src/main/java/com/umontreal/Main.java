package com.umontreal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.umontreal.utils.DirExplorer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // TO DO
        // Get file PATH from args

        File projectDir = new File("src/main/java/com/umontreal/org/javaparser/examples/");
        extractMetric(projectDir);

        csvFileGenerator("test");

    }


    public static int class_LOC(Scanner scanner){
        int loc = 0;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(!data.equals("")){
                loc++;
            }
        }
        return loc;
    }

    public static int methode_LOC(Scanner scanner){
        int loc = 0;
        Stack<Character> stack = new Stack();
        stack.push('{');
        while (!stack.isEmpty()){
            String data = scanner.nextLine();
            if(data.contains("}"))
                stack.pop();
            if(!data.equals("")){
                loc++;
            }
        }
        return loc;
    }

    //asddsa
    //asdasd
    /*
    * a sd
    * asd asd
    * asd
    *
    * asd asd
    * sad
    */
    public static int classe_methode_CLOC(Scanner scanner){
        int loc = 0;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data.contains("//") || (data.contains("/*") && data.contains("*/"))){
                loc++;
            }else if(data.contains("/*")){
                loc++;
                int leftComment = 1;
                while(leftComment != 0){
                    data = scanner.nextLine();
                    if(data.contains("/*"))
                        leftComment++;
                    if(data.contains("*/"))
                        leftComment--;
                    if(!data.equals("")){
                        loc++;
                    }
                }
            }
        }
        return loc;
    }

    public static double classe_dc(Scanner scanner){
        return classe_methode_CLOC(scanner) / class_LOC(scanner);
    }

    public static double methode_dc(Scanner scanner){
        return classe_methode_CLOC(scanner) / methode_LOC(scanner);
    }

    public static void csvFileGenerator(String path){
        createCsvFiles("classes");
        createCsvFiles("methodes");
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            for(String file : result){
                if(file.substring(file.length()-4,file.length()).equals("java")){
                    addClassEntry(file);
                    addMethodsEntry(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createCsvFiles(String fileName) {
        try {
            File file = new File(fileName+".csv");
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void addClassEntry(String file) throws FileNotFoundException {
        File f = new File(file);
        Scanner scanner = new Scanner(f);
        System.out.println(class_LOC(new Scanner(f)));
        System.out.println(classe_methode_CLOC(scanner));
    }

    private static void addMethodsEntry(String file) {

    }


    public static void extractMetric(File projectDir) throws FileNotFoundException {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));

            CompilationUnit cu = StaticJavaParser.parse(file);
            VoidVisitor<?> classVisitor = new Main.ClassVisitor();
            classVisitor.visit(cu, null);

            VoidVisitor<?> methodVisitor = new Main.MethodVisitor();
            methodVisitor.visit(cu, null);

        }).explore(projectDir);
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            System.out.println("Method Name Printed: " + md.getName());
            System.out.println("line length: " + md.getRange().map(range -> range.end.line - range.begin.line + 1).orElse(0));
            md.getComment().ifPresent(comment -> {System.out.println("JavaDoc length: " + (comment.getEnd().get().line - comment.getBegin().get().line + 1));});


            // Pass this to LOC and CLOC functions:
            System.out.println(md.getDeclarationAsString());
            System.out.println(md.getBody().get());

            // Create and collect MethodMetric Object

        }
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
            super.visit(cid, arg);
            System.out.println("Class Name Printed: " + cid.getName());
            System.out.println("line length: " + cid.getRange().map(range -> range.end.line - range.begin.line + 1).orElse(0));
            System.out.println("Begin: " + cid.getRange().get().begin.line);
            System.out.println("End: " + cid.getRange().get().end.line);
            cid.getComment().ifPresent(comment -> {System.out.println("JavaDoc length: " + (comment.getEnd().get().line - comment.getBegin().get().line + 1));});

            // Find class String and pass it to LOC and CLOC functions


            // Create and collect ClassMetric Object

        }
    }
}
