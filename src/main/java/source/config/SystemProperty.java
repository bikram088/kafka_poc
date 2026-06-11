package source.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum SystemProperty {
    APP_CONFIG_FILE("app.config.file, null, true"){
        @Override
        public void validate(String value){
            Path appConfigFilePath = Paths.get(value);

            if(!Files.exists(appConfigFilePath)){
                throw new IllegalArgumentException("Invalid");
            }

            if(!Files.isReadable(appConfigFilePath)){
                throw new IllegalArgumentException("Permission denied");
            }
        }
    };

    public abstract void validate(String value);
}
