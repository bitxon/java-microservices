package bitxon.quarkus.test;

import bitxon.quarkus.test.ext.PostgresTestResourceLifecycleManager;
import bitxon.quarkus.test.ext.WiremockTestResourceLifecycleManager;
import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(PostgresTestResourceLifecycleManager.class)
@QuarkusTestResource(WiremockTestResourceLifecycleManager.class)
public abstract class AbstractQuarkusIntTest {
    // For some reason if didn't specify this QuarkusTestResource near native-test classes
    // `gradle testNative` will fail
    // TODO https://github.com/quarkusio/quarkus/issues/27511

}
