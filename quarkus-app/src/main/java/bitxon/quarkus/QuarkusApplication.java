package bitxon.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * We need this class to simplify run from IDE
 */
@QuarkusMain
public class QuarkusApplication {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
