package sustain.geosieve.create;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {
    @Test
    public void simpleListConcat() {
        List<String> list1 = Arrays.asList("Alpha", "Beta", "Gamma");
        List<String> list2 = Arrays.asList("Delta", "Epsilon", "Zeta", "Theta");
        List<String> expected = Arrays.asList("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Theta");
        assertEquals(expected, Util.concat(list1, list2));
    }

    @Test
    public void largeListConcat() {
        int size = 1000;
        List<Integer> firstThousand = new ArrayList<>();
        List<Integer> secondThousand = new ArrayList<>();
        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i <= size * 2; i++) {
            ((i <= size) ? firstThousand : secondThousand).add(i);
            expected.add(i);
        }
        assertEquals(expected, Util.concat(firstThousand, secondThousand));
    }

    @Test
    public void simpleMap() {
        List<String> list1 = Arrays.asList("Alpha", "Beta", "Gamma");
        List<String> mapped = Util.map(list1, String::toLowerCase);
        List<String> expected = Arrays.asList("alpha", "beta", "gamma");
        assertEquals(expected, mapped);
    }

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
