package com.umontreal.bean;

public class ClassMetric {
    public String path;
    public String name;
    public int LOC;
    public int CLOC;
    public float DC;
    public int wmc;;
    public float BC;


    public ClassMetric(String path, String name, int LOC, int CLOC, int wmc){
        this.path = path;
        this.name = name;
        this.LOC = LOC;
        this.CLOC = CLOC;
        this.DC = (float) CLOC / LOC;
        this.wmc = wmc;
        if (wmc == 0) {
            this.BC = 0;
        } else {
            this.BC = this.DC / wmc;
        }
    }

    @Override
    public String toString() {
        return  path+","+name+","+LOC+","+CLOC+","+DC+","+wmc+","+BC;
    }
}
