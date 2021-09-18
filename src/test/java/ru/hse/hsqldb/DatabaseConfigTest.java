package ru.hse.hsqldb;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseConfigTest {

    @Test
    public void correctFile() throws IOException {
        Path path = Files.createTempFile("config", ".json");
        String content = """
                {
                    "url": "jdbc:hsqldb:file:testing",
                    "user": "andreyshspb",
                    "password": "password"
                }
                """;
        Files.writeString(path, content);

        DatabaseConfig config = new DatabaseConfig(path.toAbsolutePath().toString());
        Assertions.assertEquals("jdbc:hsqldb:file:testing", config.getUrl());
        Assertions.assertEquals("andreyshspb", config.getUser());
        Assertions.assertEquals("password", config.getPassword());
    }

    @Test
    public void incorrectFile() throws IOException {
        Path path = Files.createTempFile("config", ".json");
        String content = """
                {
                    "url": "jdbc:hsqldb:file:testing",
                    "user": "andreyshspb"
                }
                """;
        Files.writeString(path, content);

        Assertions.assertThrows(
                JSONException.class,
                () -> new DatabaseConfig(path.toAbsolutePath().toString())
        );
    }

}
