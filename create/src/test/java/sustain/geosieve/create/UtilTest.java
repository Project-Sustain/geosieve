package sustain.geosieve.create;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {
    @Test
    public void doThreadsRunsAndJoinsProperly() {
        int size = 1_000;
        Set<Integer> numbers = Collections.synchronizedSet(new HashSet<>());
        Set<Integer> expected = new HashSet<>();
        for (int i = 0; i < size; i++) {
            expected.add(i);
        }
        Util.doThreads((Integer i) -> () -> numbers.add(i), expected);
        assertEquals(expected, numbers);
    }

    @Test
    public void doNThreadsRunsAndJoinsProperly() {
        int size = 1_000;
        Set<Integer> numbers = Collections.synchronizedSet(new HashSet<>());
        Set<Integer> expected = new HashSet<>();

        Random r = new Random(2270);
        for (int i = 0; i < size; i++) {
            expected.add(r.nextInt());
        }

        Random r2 = new Random(2270);
        Util.doThreads(() -> () -> {
            for (int i = 0; i < 10; i++) {
                numbers.add(r2.nextInt());
            }
        }, size / 10);
        assertEquals(expected, numbers);
    }
}
