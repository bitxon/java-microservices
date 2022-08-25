package bitxon.quarkus;

import bitxon.quarkus.ext.PostgresTestResourceLifecycleManager;
import bitxon.quarkus.ext.WiremockTestResourceLifecycleManager;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(PostgresTestResourceLifecycleManager.class)
@QuarkusTestResource(WiremockTestResourceLifecycleManager.class)
public abstract class AbstractQuarkusIntTest {
    // For some reason if didn't specify this QuarkusTestResource near native-test classes
    // `gradle testNative` will fail
    // TODO research

}
