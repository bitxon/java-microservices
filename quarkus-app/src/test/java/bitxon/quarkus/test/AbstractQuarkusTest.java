package bitxon.quarkus.test;

import bitxon.quarkus.test.ext.PostgresTestResourceLifecycleManager;
import bitxon.quarkus.test.ext.WiremockTestResourceLifecycleManager;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(PostgresTestResourceLifecycleManager.class)
@QuarkusTestResource(WiremockTestResourceLifecycleManager.class)
public abstract class AbstractQuarkusTest {

}
