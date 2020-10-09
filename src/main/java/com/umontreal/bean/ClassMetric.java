package com.umontreal.bean;

/**
 * The class ClassMetric
 * Contains and compute all the metrics relevant to a Java class.
 */
public class ClassMetric {
    private final String path;
    private final String name;
    private final int LOC;
    private final int CLOC;
    private final float DC;
    private final int wmc;
    private final float BC;

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

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getLOC() {
        return LOC;
    }

    public int getCLOC() {
        return CLOC;
    }

    public float getDC() {
        return DC;
    }

    public int getWmc() {
        return wmc;
    }

    public float getBC() {
        return BC;
    }

    @Override
    public String toString() {
        return  path+","+name+","+LOC+","+CLOC+","+DC+","+wmc+","+BC;
    }
}
