package com.umontreal;

import java.util.Scanner;
import java.util.Stack;

public class Main {

    public int class_LOC(Scanner scanner){
        int loc = 0;
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(!data.equals("")){
                loc++;
            }
        }
        return loc;
    }

    public int methode_LOC(Scanner scanner){
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
    public int classe_methode_CLOC(Scanner scanner){
        int loc = 0;

        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            if(data.contains("//") || (data.contains("/*") && data.contains("*/"))){
                loc++;
            }else if(data.contains("/*")){
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

    public double classe_dc(Scanner scanner){
        return classe_methode_CLOC(scanner) / class_LOC(scanner);
    }

    public double methode_dc(Scanner scanner){
        return classe_methode_CLOC(scanner) / methode_LOC(scanner);
    }
}
