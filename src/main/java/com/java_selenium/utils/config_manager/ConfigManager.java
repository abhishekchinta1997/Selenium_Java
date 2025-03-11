package com.java_selenium.utils.config_manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    private static final String CONFIG_FILE = "com/java_selenium/resources/config.properties";

    static
    {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE))
        {
            // Debug: Print absolute path
            URL res = ConfigManager.class.getClassLoader().getResource(CONFIG_FILE);
            System.out.println("Loading config from: " + (res != null ? res.getPath() : "NULL"));

            if (input == null)
            {
                throw new RuntimeException("File not found: " + CONFIG_FILE +
                        "\nVerify: target/test-classes/com/java_selenium/resources/config.properties exists!");
            }
            properties.load(input);
            System.out.println("SUCCESS: Loaded " + CONFIG_FILE);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error loading " + CONFIG_FILE, e);
        }
    }

    public static String getProperty(String key)
    {
        return properties.getProperty(key);
    }
}