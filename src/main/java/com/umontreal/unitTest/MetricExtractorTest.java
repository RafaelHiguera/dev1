package com.umontreal.unitTest;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import static com.umontreal.utils.MetricExtractor.*;

public class MetricExtractorTest {
    protected Scanner testingClass;
    protected Scanner testingFunction;

    @Before
    public void setUp() throws FileNotFoundException {
        System.out.println(Paths.get("").toString());
        testingFunction = new Scanner(new File(".\\src\\main\\java\\com\\umontreal\\unitTest\\FunctionTest.txt"));
        testingClass = new Scanner(new File(".\\src\\main\\java\\com\\umontreal\\unitTest\\ClassTest.txt"));
    }

    @Test
    public void testClassLoc() {
        int classResultLoc = 10;
        assert(classResultLoc == class_LOC(testingClass));
    }

    @Test
    public void testClassCLoc() {
        int classResultCLoc = 6;
        assert(classResultCLoc == class_method_CLOC(testingClass));
    }

    @Test
    public void testMethodLOC() {
        int methodResultLoc = 18;
        assert(methodResultLoc == method_LOC(testingFunction));
    }

    @Test
    public void testMethodCLOC() {
        int methodResultCLoc = 6;
        assert(methodResultCLoc == class_method_CLOC(testingFunction));
    }

    @Test
    public void testCcMcCabe() {
        int[] ccMcCabeResult = new int[]{3,3,3};
        assert(Arrays.equals(ccMcCabeResult, ccMcCabe(testingFunction)));
    }


}
