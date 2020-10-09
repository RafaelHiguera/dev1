package com.umontreal.bean;

public class MethodMetric {

    public String path;
    public String className;
    public String name;
    public int LOC;
    public int CLOC;
    public float DC;
    public  int[] CC;
    public float BC;


    public MethodMetric(String path, String className, String name, int LOC, int CLOC, int[] CC){
        this.path = path;
        this.className = className;
        this.name = name;
        this.LOC = LOC;
        this.CLOC = CLOC;
        this.DC = (float) CLOC / LOC;
        this.CC = CC;
        if (CC[IMetric.methodInUseCC] == 0) {
            this.BC = 0;
        } else {
            this.BC = this.DC / CC[IMetric.methodInUseCC];
        }
    }

    @Override
    public String toString() {
        return  path+","+className+","+name+","+LOC+","+CLOC+","+DC+","+CC[IMetric.methodInUseCC]+","+BC;
    }
}
