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

public class Main {

    public static void main(String[] args) {
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
}
