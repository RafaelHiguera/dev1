package com.umontreal.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvPrinter {

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
