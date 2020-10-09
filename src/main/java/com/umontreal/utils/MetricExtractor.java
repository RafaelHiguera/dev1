package com.umontreal.utils;

import java.util.Scanner;

public class MetricExtractor {

    public static int class_LOC(Scanner scanner){
        int loc = 0;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data.equals(""))
                continue;
            loc++;
        }
        return loc;
    }

    public static int method_LOC(Scanner scanner){
        int loc = 0;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data.equals(""))
                continue;
            loc++;
        }
        return loc;
    }

    public static int class_method_CLOC(Scanner scanner){
        int cloc = 0;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data.equals(""))
                continue;
            if(data.contains("//") || (data.contains("/*") && data.contains("*/"))){
                cloc++;
            }else if(data.contains("/*")){
                cloc++;
                int leftComment = 1;
                while(leftComment != 0){
                    data = scanner.nextLine();
                    if(data.contains("/*"))
                        leftComment++;
                    if(data.contains("*/"))
                        leftComment--;
                    cloc++;
                }
            }
        }
        return cloc;
    }

    // [0] :  e – n + 2 ; [1] :  1 + d; [2] : r;
    public static int[] ccMcCabe(Scanner scanner){
        scanner.nextLine();
        int e = 0, n = 0, d = 0, r = 1;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data.equals("") || data.equals(" ") || lineDoesNotContainLetters(data) || data.startsWith("//"))
                continue;
            else if(data.startsWith("/*")){
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
//        System.out.println(e+" "+n+" "+d+" "+r);
        return new int[]{e - n + 2, 1+d, r};
    }

    private static boolean lineDoesNotContainLetters(String line){
        for(char c : line.toCharArray()){
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                return false;
        }
        return true;
    }

    public static void skipLines(Scanner s,int lineNum){
        for(int i = 0; i < lineNum;i++){
            if(s.hasNextLine())s.nextLine();
        }
    }
}
