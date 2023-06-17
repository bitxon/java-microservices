package gatling.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {


    public static String peekRandom(List<String> list) {
        var rand = ThreadLocalRandom.current();
        return list.get(rand.nextInt(list.size()));
    }
}
