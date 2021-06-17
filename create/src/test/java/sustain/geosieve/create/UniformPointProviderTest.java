package sustain.geosieve.create;

import org.junit.jupiter.api.Test;
import sustain.geosieve.create.uniform.Extents;
import sustain.geosieve.create.uniform.UniformPointProvider;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import static sustain.geosieve.create.Util.*;

public class UniformPointProviderTest {
    private static final Extents simple = new Extents(1, 1, 0, 0);
    private static final Extents complex = new Extents(1.6, 3.141592685, 7.4321165, 6.282);
    @Test
    public void basicPoints() {
        UniformPointProvider provider = new UniformPointProvider(0.2, 0.3, simple);

        HashSet<LatLng> points = new HashSet<>();
        for (LatLng point : provider) {
            points.add(point);
        }
        assertTrue(setsCloseEnough(points, new HashSet<>(Arrays.asList(
                new LatLngPoint(0, 0),
                new LatLngPoint(0.2, 0),
                new LatLngPoint(0.4, 0),
                new LatLngPoint(0.6, 0),
                new LatLngPoint(0.8, 0),
                new LatLngPoint(0, 0.3),
                new LatLngPoint(0.2, 0.3),
                new LatLngPoint(0.4, 0.3),
                new LatLngPoint(0.6, 0.3),
                new LatLngPoint(0.8, 0.3),
                new LatLngPoint(0, 0.6),
                new LatLngPoint(0.2, 0.6),
                new LatLngPoint(0.4, 0.6),
                new LatLngPoint(0.6, 0.6),
                new LatLngPoint(0.8, 0.6),
                new LatLngPoint(0, 0.9),
                new LatLngPoint(0.2, 0.9),
                new LatLngPoint(0.4, 0.9),
                new LatLngPoint(0.6, 0.9),
                new LatLngPoint(0.8, 0.9)
        ))));
    }

    @Test
    public void alignedStepPoints() {
        UniformPointProvider provider = new UniformPointProvider(0.1, 0.1, simple);

        HashSet<LatLng> points = new HashSet<>();
        for (LatLng point : provider) {
            points.add(point);
        }
        assertTrue(setsCloseEnough(points, new HashSet<>(Arrays.asList(
                // YES THIS IS NECESSARY ITS THE ONLY WAY I COULD RELIABLY REPRODUCE THIS PROBLEM
                new LatLngPoint(0.0, 0), new LatLngPoint(0.1, 0), new LatLngPoint(0.2, 0),
                new LatLngPoint(0.3, 0), new LatLngPoint(0.4, 0), new LatLngPoint(0.5, 0),
                new LatLngPoint(0.6, 0), new LatLngPoint(0.7, 0), new LatLngPoint(0.8, 0),
                new LatLngPoint(0.9, 0), new LatLngPoint(0.0, 0.1), new LatLngPoint(0.1, 0.1),
                new LatLngPoint(0.2, 0.1), new LatLngPoint(0.3, 0.1), new LatLngPoint(0.4, 0.1),
                new LatLngPoint(0.5, 0.1), new LatLngPoint(0.6, 0.1), new LatLngPoint(0.7, 0.1),
                new LatLngPoint(0.8, 0.1), new LatLngPoint(0.9, 0.1), new LatLngPoint(0.0, 0.2),
                new LatLngPoint(0.1, 0.2), new LatLngPoint(0.2, 0.2), new LatLngPoint(0.3, 0.2),
                new LatLngPoint(0.4, 0.2), new LatLngPoint(0.5, 0.2), new LatLngPoint(0.6, 0.2),
                new LatLngPoint(0.7, 0.2), new LatLngPoint(0.8, 0.2), new LatLngPoint(0.9, 0.2),
                new LatLngPoint(0.0, 0.3), new LatLngPoint(0.1, 0.3), new LatLngPoint(0.2, 0.3),
                new LatLngPoint(0.3, 0.3), new LatLngPoint(0.4, 0.3), new LatLngPoint(0.5, 0.3),
                new LatLngPoint(0.6, 0.3), new LatLngPoint(0.7, 0.3), new LatLngPoint(0.8, 0.3),
                new LatLngPoint(0.9, 0.3), new LatLngPoint(0.0, 0.4), new LatLngPoint(0.1, 0.4),
                new LatLngPoint(0.2, 0.4), new LatLngPoint(0.3, 0.4), new LatLngPoint(0.4, 0.4),
                new LatLngPoint(0.5, 0.4), new LatLngPoint(0.6, 0.4), new LatLngPoint(0.7, 0.4),
                new LatLngPoint(0.8, 0.4), new LatLngPoint(0.9, 0.4), new LatLngPoint(0.0, 0.5),
                new LatLngPoint(0.1, 0.5), new LatLngPoint(0.2, 0.5), new LatLngPoint(0.3, 0.5),
                new LatLngPoint(0.4, 0.5), new LatLngPoint(0.5, 0.5), new LatLngPoint(0.6, 0.5),
                new LatLngPoint(0.7, 0.5), new LatLngPoint(0.8, 0.5), new LatLngPoint(0.9, 0.5),
                new LatLngPoint(0.0, 0.6), new LatLngPoint(0.1, 0.6), new LatLngPoint(0.2, 0.6),
                new LatLngPoint(0.3, 0.6), new LatLngPoint(0.4, 0.6), new LatLngPoint(0.5, 0.6),
                new LatLngPoint(0.6, 0.6), new LatLngPoint(0.7, 0.6), new LatLngPoint(0.8, 0.6),
                new LatLngPoint(0.9, 0.6), new LatLngPoint(0.0, 0.7), new LatLngPoint(0.1, 0.7),
                new LatLngPoint(0.2, 0.7), new LatLngPoint(0.3, 0.7), new LatLngPoint(0.4, 0.7),
                new LatLngPoint(0.5, 0.7), new LatLngPoint(0.6, 0.7), new LatLngPoint(0.7, 0.7),
                new LatLngPoint(0.8, 0.7), new LatLngPoint(0.9, 0.7), new LatLngPoint(0.0, 0.8),
                new LatLngPoint(0.1, 0.8), new LatLngPoint(0.2, 0.8), new LatLngPoint(0.3, 0.8),
                new LatLngPoint(0.4, 0.8), new LatLngPoint(0.5, 0.8), new LatLngPoint(0.6, 0.8),
                new LatLngPoint(0.7, 0.8), new LatLngPoint(0.8, 0.8), new LatLngPoint(0.9, 0.8),
                new LatLngPoint(0.0, 0.9), new LatLngPoint(0.1, 0.9), new LatLngPoint(0.2, 0.9),
                new LatLngPoint(0.3, 0.9), new LatLngPoint(0.4, 0.9), new LatLngPoint(0.5, 0.9),
                new LatLngPoint(0.6, 0.9), new LatLngPoint(0.7, 0.9), new LatLngPoint(0.8, 0.9),
                new LatLngPoint(0.9, 0.9)
        ))));
    }

    @Test
    public void complicatedPoints() {
        UniformPointProvider provider = new UniformPointProvider(0.81, 1.57, complex);

        HashSet<LatLng> points = new HashSet<>();
        for (LatLng point : provider) {
            points.add(point);
        }
        assertTrue(setsCloseEnough(points, new HashSet<>(Arrays.asList(
                new LatLngPoint(3.141592685, 1.6),
                new LatLngPoint(3.951592685, 1.6),
                new LatLngPoint(4.761592685, 1.6),
                new LatLngPoint(5.571592685, 1.6),
                new LatLngPoint(3.141592685, 3.17),
                new LatLngPoint(3.951592685, 3.17),
                new LatLngPoint(4.761592685, 3.17),
                new LatLngPoint(5.571592685, 3.17),
                new LatLngPoint(3.141592685, 4.74),
                new LatLngPoint(3.951592685, 4.74),
                new LatLngPoint(4.761592685, 4.74),
                new LatLngPoint(5.571592685, 4.74),
                new LatLngPoint(3.141592685, 6.31),
                new LatLngPoint(3.951592685, 6.31),
                new LatLngPoint(4.761592685, 6.31),
                new LatLngPoint(5.571592685, 6.31)
        ))));
    }

    @Test
    public void multipleThreadsCanConsume() {
        final int numThreads = 5;

        final UniformPointProvider provider = new UniformPointProvider(0.2, 0.3, simple);
        final Set<LatLng> points = Collections.synchronizedSet(new HashSet<>());
        final List<Thread> threads = new ArrayList<>(numThreads);

        for (int i = 0; i < numThreads; i++) {
            threads.add(new Thread(() -> {
                for (LatLng point : provider) {
                    points.add(point);
                }
            }));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) { }
        }

        assertTrue(setsCloseEnough(points, new HashSet<>(Arrays.asList(
                new LatLngPoint(0, 0),
                new LatLngPoint(0.2, 0),
                new LatLngPoint(0.4, 0),
                new LatLngPoint(0.6, 0),
                new LatLngPoint(0.8, 0),
                new LatLngPoint(0, 0.3),
                new LatLngPoint(0.2, 0.3),
                new LatLngPoint(0.4, 0.3),
                new LatLngPoint(0.6, 0.3),
                new LatLngPoint(0.8, 0.3),
                new LatLngPoint(0, 0.6),
                new LatLngPoint(0.2, 0.6),
                new LatLngPoint(0.4, 0.6),
                new LatLngPoint(0.6, 0.6),
                new LatLngPoint(0.8, 0.6),
                new LatLngPoint(0, 0.9),
                new LatLngPoint(0.2, 0.9),
                new LatLngPoint(0.4, 0.9),
                new LatLngPoint(0.6, 0.9),
                new LatLngPoint(0.8, 0.9)
        ))));
    }
}
