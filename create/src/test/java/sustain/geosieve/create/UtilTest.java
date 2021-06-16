package sustain.geosieve.create;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
