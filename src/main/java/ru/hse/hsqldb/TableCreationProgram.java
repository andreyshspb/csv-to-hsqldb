package ru.hse.hsqldb;

import org.apache.commons.cli.*;

public class TableCreationProgram {

    public static void main(String[] args) {
        Options options = new Options();

        Option config = new Option("c", "config", true, "database config");
        config.setRequired(true);
        options.addOption(config);

        Option file = new Option("f", "file", true, "csv file with source");
        file.setRequired(true);
        options.addOption(file);

        Option table = new Option("t", "table", true, "name of storage table");
        table.setRequired(true);
        options.addOption(table);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException exception) {
            System.out.println(exception.getMessage());
            formatter.printHelp("utility-name", options);
            return;
        }

        String databaseConfigFileName = cmd.getOptionValue("config");
        String csvFileName = cmd.getOptionValue("file");
        String tableName = cmd.getOptionValue("table");

        TableCreationProgram.createFromCSV(databaseConfigFileName, csvFileName, tableName);
    }

    public static void createFromCSV(String databaseConfigFileName,
                                     String csvFileName,
                                     String tableName) {
        try {
            DatabaseConfig config = new DatabaseConfig(databaseConfigFileName);
            Database database = new Database(config);
            database.createTableFromCSV(csvFileName, tableName);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
