package com.umontreal.bean;

public class ClassMetric {
    public String path;
    public String name;
    public int LOC;
    public int CLOC;
    public float DC;


    public ClassMetric(String path, String name, int LOC, int CLOC){
        this.path = path;
        this.name = name;
        this.LOC = LOC;
        this.CLOC = CLOC;
        this.DC = (float) CLOC / LOC;
    }

    @Override
    public String toString() {
        return  path+","+name+","+LOC+","+CLOC+","+DC;
    }
}
