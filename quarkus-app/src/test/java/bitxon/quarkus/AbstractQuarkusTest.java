package bitxon.quarkus;

import bitxon.quarkus.ext.PostgresTestResourceLifecycleManager;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(PostgresTestResourceLifecycleManager.class)
public abstract class AbstractQuarkusTest {

}
