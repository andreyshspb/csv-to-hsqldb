package ru.hse.hsqldb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableCreationProgramTest {

    @Test
    void createTable() throws IOException {
        Path configPath = Files.createTempFile("config", ".json");
        String configContent = """
                {
                    "url": "jdbc:hsqldb:mem:testing",
                    "user": "andreyshspb",
                    "password": "password"
                }
                """;
        Files.writeString(configPath, configContent);

        Path csvPath = Files.createTempFile("file", ".csv");
        String csvContent = """
                name,age,university,comment
                Andrey,18,HSE,"yandex intern"
                Rita,25,SPbSU,"no, she is not Margarita"
                """;
        Files.writeString(csvPath, csvContent);

        TableCreationProgram.createFromCSV(
                configPath.toAbsolutePath().toString(),
                csvPath.toAbsolutePath().toString(),
                "students"
        );

        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:hsqldb:mem:testing",
                        "andreyshspb",
                        "password"
                );
                Statement statement = connection.createStatement()
        ) {
            ResultSet result = statement.executeQuery("select * from students;");

            List<String> first = new ArrayList<>();
            first.add("Andrey"); first.add("18"); first.add("HSE"); first.add("yandex intern");
            List<String> second = new ArrayList<>();
            second.add("Rita"); second.add("25"); second.add("SPbSU"); second.add("no, she is not Margarita");
            List<List<String>> expected = new ArrayList<>();
            expected.add(first); expected.add(second);

            int rowNumber = 0;
            while (result.next()) {
                List<String> tested = new ArrayList<>();
                tested.add(result.getString("name"));
                tested.add(result.getString("age"));
                tested.add(result.getString("university"));
                tested.add(result.getString("comment"));

                Assertions.assertEquals(expected.get(rowNumber), tested);

                rowNumber++;
            }

            Assertions.assertEquals(expected.size(), rowNumber);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
