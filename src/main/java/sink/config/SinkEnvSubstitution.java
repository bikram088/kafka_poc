package sink.config;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class SinkEnvSubstitution {
    private static final StringSubstitutor substitutor;

    static{
        Map<String, String> envVars = System.getenv();
        substitutor = new StringSubstitutor(envVars);
        substitutor.setEnableUndefinedVariableException(false);
    }

    public static String resolve(String value){
        if(value == null || !value.contains("${")) return value;
        return substitutor.replace(value);
    }

    public static String resolvePath(String path){
        if(path == null || !path.contains("${")) return path;
        String resolved = Objects.replace(path);
        if(resolved == null || resolved.trim().isEmpty()) return resolved;
        return Paths.get(resolved).normalize().toString();
    }
}
