package ru.hse.hsqldb;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseConfig {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig(String fileName) throws IOException {
        String jsonString = Files.readString(Path.of(fileName), Charset.defaultCharset());
        JSONObject json = new JSONObject(jsonString);
        url = json.getString("url");
        user = json.getString("user");
        password = json.getString("password");
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
