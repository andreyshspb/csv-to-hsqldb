package ru.hse.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private final DatabaseConfig config;

    public Database(DatabaseConfig config) {
        this.config = config;
    }

    public void createTableFromCSV(String fileName, String tableName) {
        try (
                Connection connection = DriverManager.getConnection(
                        config.getUrl(),
                        config.getUser(),
                        config.getPassword()
                );
                Statement statement = connection.createStatement()
        ) {
            CSVWrapper csvWrapper = new CSVWrapper(fileName);
            String creationQuery = csvWrapper.getCreationQuery(tableName);
            statement.executeQuery(creationQuery);
            String insertionQuery;
            while ((insertionQuery = csvWrapper.getNextInsertionQuery(tableName)) != null) {
                statement.executeQuery(insertionQuery);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
