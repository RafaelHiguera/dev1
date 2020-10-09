package com.umontreal.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * The Class CsvPrinter.
 * Contains helper method to print to a csv file.
 */
public class CsvPrinter {

    /**
     * Print metrics to csv.
     * The toString() method of the objects in the listToPrint must be csv friendly.
     *
     * @param fileName    the file name
     * @param columnNames the column names
     * @param listToPrint the list to print
     */
    public static void printMetricsToCSV(String fileName, String columnNames, List<?> listToPrint) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(columnNames).append("\n");

        for (Object obj: listToPrint) {
            builder.append(obj.toString()).append(",");
            builder.append('\n');
        }

        assert pw != null;
        pw.write(builder.toString());
        pw.close();
    }
}
