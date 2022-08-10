package bitxon.dropwizard.customization;

import java.io.IOException;
import java.io.InputStream;

import io.dropwizard.configuration.FileConfigurationSourceProvider;

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
