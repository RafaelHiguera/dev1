package com.umontreal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.umontreal.bean.ClassMetric;
import com.umontreal.bean.MethodMetric;
import com.umontreal.utils.DirExplorer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

public class Main {

    public static void main(String[] args) {
        // Pass this in arguments for tests:
        // "src/main/java/com/umontreal/org/javaparser/examples/"
        csvFileGenerator("test");
    }

    public static void printClassToCSV(String fileName, String columnNames, List<?> listToPrint) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(columnNames).append("\n");

        for (Object obj: listToPrint) {
            builder.append(obj.toString()).append(",");
            builder.append('\n');
        }

        assert pw != null;
        pw.write(builder.toString());
        pw.close();
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
        System.out.println(Arrays.toString(ccMcCabe(scanner)));
    }

    private static void addMethodsEntry(String file) {

    }

    // [0] :  e – n + 2 ; [1] :  1 + d; [2] : r;
    public static int[] ccMcCabe(Scanner scanner){
        scanner.nextLine();
        int e = 0, n = 0, d = 0, r = 1;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data == "" || data == " " || lineDoesNotContainLetters(data) || data.substring(0,2).equals("//"))
                continue;
            else if(data.substring(0,2).equals("/*")){
                while(!data.contains("*/")){
                    data = scanner.nextLine();
                }
                data = scanner.nextLine();
            }
            n++;
            if(!data.contains("break") || !data.contains("default") || data.contains("else") || data.contains("continue"))
                e++;
            if(data.contains("if") || data.contains("else if") || data.contains("switch") || data.contains("case")){
                e++;
                d++;
                r++;
            }
            if(data.contains("while") || data.contains("for")){
                d++;
                r++;
            }
            if(!scanner.hasNextLine())
                e--;
        }
        System.out.println(e+" "+n+" "+d+" "+r);
        return new int[]{e - n + 2, 1+d, r};
    }

    private static boolean lineDoesNotContainLetters(String line){
        for(char c : line.toCharArray()){
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                return false;
        }
        return true;
    }


    public static void extractMetric(File projectDir, List<MethodMetric> methodMetricsList, List<ClassMetric> classMetricList) throws FileNotFoundException {

        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            CompilationUnit cu = StaticJavaParser.parse(file);

            List<ClassMetric> classMetricListFromFile = new ArrayList<>();
            VoidVisitor<List<ClassMetric>> classVisitor = new Main.ClassVisitor(file);
            classVisitor.visit(cu, classMetricListFromFile);
            classMetricList.addAll(classMetricListFromFile);

            List<MethodMetric> methodMetricsListFromFile = new ArrayList<>();
            VoidVisitor<List<MethodMetric>> methodVisitor = new Main.MethodVisitor(file);
            methodVisitor.visit(cu, methodMetricsListFromFile);
            methodMetricsList.addAll(methodMetricsListFromFile);

        }).explore(projectDir);
    }

    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodMetric>> {
        private File file;

        public MethodVisitor(File file) { this.file = file; }

        public void visit(MethodDeclaration md, List<MethodMetric> collector){
            super.visit(md, collector);

            String path = this.file.getPath();
            String methodName = reformatMethodName(md.getDeclarationAsString(false, false, false));

            Scanner scanner = new Scanner(md.getComment() + md.getDeclarationAsString() + md.getBody().get().toString());
            int numberOfLines = methode_LOC(scanner);
            scanner = new Scanner(md.getComment() + md.getDeclarationAsString() + md.getBody().get().toString());
            int numberOfLinesWithComment = classe_methode_CLOC(scanner);

            // Create and collect MethodMetric Object
            MethodMetric methodMetric = new MethodMetric(path, methodName, numberOfLines, numberOfLines + numberOfLinesWithComment);
            collector.add(methodMetric);
        }

        public String reformatMethodName(String methodDeclaration) {
            methodDeclaration = methodDeclaration.split(" ", 2)[1];
            methodDeclaration = methodDeclaration.replace("(", "_");
            methodDeclaration = methodDeclaration.replace(", ", "_");
            methodDeclaration = methodDeclaration.replace(")", "");
            return methodDeclaration;
        }
    }

    public static void skipLines(Scanner s,int lineNum){
        for(int i = 0; i < lineNum;i++){
            if(s.hasNextLine())s.nextLine();
        }
    }

    private static class ClassVisitor extends VoidVisitorAdapter<List<ClassMetric>> {
        private File file;

        public ClassVisitor(File file) {
            this.file = file;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration cid, List<ClassMetric> collector) {
            super.visit(cid, collector);
            String path = this.file.getPath();
            String className = cid.getName().asString();

            // Find class String and pass it to LOC and CLOC functions
            Scanner scanner = null;
            try {
                int beginLine = cid.getBegin().get().line;
                scanner = new Scanner(this.file);
                skipLines(scanner, beginLine -2 );
                int numberOfLines = class_LOC(scanner);
                scanner = new Scanner(this.file);

                skipLines(scanner, beginLine -2 );
                int numberOfLinesWithComment = classe_methode_CLOC(scanner);

                // Create and collect ClassMetric Object
                ClassMetric classMetric = new ClassMetric(path, className, numberOfLines, numberOfLines + numberOfLinesWithComment);
                collector.add(classMetric);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
