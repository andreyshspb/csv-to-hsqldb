package ru.hse.hsqldb;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CSVWrapper {

    private final String[] columns;
    private final CSVReader reader;

    // TODO: create self class for exception (CSVWrapperException)
    // TODO: maybe we need to check correctness of csv file
    //       but it may be expensive
    public CSVWrapper(String fileName) throws IOException {
        reader = new CSVReader(new FileReader(fileName), ',', '"', 0);
        columns = reader.readNext();
        if (columns == null) {
            throw new IOException(fileName + " is empty");
        }
    }

    public String getCreationQuery(String tableName) {
        return Arrays.stream(columns)
                .map(name -> name + " VARCHAR(255)")
                .collect(Collectors.joining(
                        ",\n    ",
                        "CREATE TABLE " + tableName + " (\n    ",
                        "\n);\n"
                ));
    }

    public String getNextInsertionQuery(String tableName) throws IOException {
        String[] row = reader.readNext();
        if (row == null) {
            return null;
        }
        return Arrays.stream(row)
                .map(cell -> "'" + cell + "'")
                .collect(Collectors.joining(
                        ", ",
                        "INSERT INTO " + tableName + " VALUES (",
                        ");"
                ));
    }

    public String getAllInsertionQuery(String tableName) throws IOException {
        StringBuilder result = new StringBuilder();
        String insertionQuery;
        while ((insertionQuery = getNextInsertionQuery(tableName)) != null) {
            result.append(insertionQuery).append('\n');
        }
        return result.toString();
    }
}
