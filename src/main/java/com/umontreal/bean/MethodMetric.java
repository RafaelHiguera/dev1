package com.umontreal.bean;

public class MethodMetric {

    public String path;
    public String className;
    public String name;
    public int LOC;
    public int CLOC;
    public float DC;


    public MethodMetric(String path, String className, String name, int LOC, int CLOC){
        this.path = path;
        this.className = className;
        this.name = name;
        this.LOC = LOC;
        this.CLOC = CLOC;
        this.DC = (float) CLOC / LOC;
    }

    @Override
    public String toString() {
        return  path+","+className+","+name+","+LOC+","+CLOC+","+DC;
    }
}
