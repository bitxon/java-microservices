package bitxon.dropwizard.customization;

import io.dropwizard.configuration.FileConfigurationSourceProvider;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathOrFileConfigurationSourceProvider extends FileConfigurationSourceProvider {

    @Override
    public InputStream open(String path) throws IOException {
        if (path != null && path.startsWith("classpath:")) {
            return getResourceAsStream(path.substring(10));
        } else {
            return super.open(path);
        }
    }

    private InputStream getResourceAsStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }
}
