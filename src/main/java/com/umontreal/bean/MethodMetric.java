package com.umontreal.bean;

/**
 * The class MethodMetric
 * Contains and compute all the metrics relevant to a Java method.
 */
public class MethodMetric {
    private final String path;
    private final String className;
    private final String name;
    private final int LOC;
    private final int CLOC;
    private final float DC;
    private final int[] CC;
    private final float BC;

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

    public String getPath() {
        return path;
    }

    public String getClassName() {
        return className;
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

    public int[] getCC() {
        return CC;
    }

    public float getBC() {
        return BC;
    }

    @Override
    public String toString() {
        return  path+","+className+","+name+","+LOC+","+CLOC+","+DC+","+CC[IMetric.methodInUseCC]+","+BC;
    }
}
