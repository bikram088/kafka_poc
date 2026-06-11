package source.config.api;

import source.config.SystemProperty;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ApiPropertiesLoader {
    private static final Properties props = new Properties();

    static{
        try{
            Path path = Paths.get(SystemProperty.API_CONFIG_FILE.getValue());

            try(InputStream in = Files.newInputStream(path)){
                props.load(in);
            }
        }catch (Exception e){
            throw new RuntimeException("Failed to load db properties");
        }
    }

    public static String getProperty(String key){
        return props.getProperty(key);
    }
}
