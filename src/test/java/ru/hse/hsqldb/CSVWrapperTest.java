package ru.hse.hsqldb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVWrapperTest {

    @Test
    public void onlyColumns() throws IOException {
        Path path = Files.createTempFile("data", ".csv");
        String content = """
                name,age,university
                """;
        Files.writeString(path, content);

        CSVWrapper wrapper = new CSVWrapper(path.toAbsolutePath().toString());
        String expected = """
                CREATE TABLE students (
                    name VARCHAR(32),
                    age VARCHAR(32),
                    university VARCHAR(32)
                );
                """;
        Assertions.assertEquals(expected, wrapper.getCreationQuery("students"));
        Assertions.assertNull(wrapper.getNextInsertionQuery("students"));
    }

    @Test
    public void smallFile() throws IOException {
        Path path = Files.createTempFile("data", ".csv");
        String content = """
                name,age,university,comment
                Andrey,18,HSE,"yandex intern"
                Rita,25,SPbSU,"no, she is not Margarita"
                """;
        Files.writeString(path, content);

        CSVWrapper wrapper = new CSVWrapper(path.toAbsolutePath().toString());
        String expected = """
                INSERT INTO students VALUES ('Andrey', '18', 'HSE', 'yandex intern');
                INSERT INTO students VALUES ('Rita', '25', 'SPbSU', 'no, she is not Margarita');
                """;
        Assertions.assertEquals(expected, wrapper.getAllInsertionQuery("students"));
        Assertions.assertNull(wrapper.getNextInsertionQuery("students"));
    }

    @Test
    public void emptyFile() throws IOException {
        Path path = Files.createTempFile("data", ".csv");
        String content = "";
        Files.writeString(path, content);

        Assertions.assertThrows(
                IOException.class,
                () -> new CSVWrapper(path.toAbsolutePath().toString())
        );
    }

}
