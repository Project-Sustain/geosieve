package sustain.geosieve.create;

import sustain.geosieve.create.uniform.GridWorker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Util {
    public static boolean doubleCloseEnough(double first, double second) {
        // evil fixed epsilon, sue me
        return Math.abs(first - second) < 0.0001;
    }

    public static boolean setsCloseEnough(Collection<LatLng> first, Collection<LatLng> second) {
        if (first.size() != second.size()) {
            return false;
        }
        for (LatLng f : first) {
            boolean isMatchingPoint = false;
            for (LatLng s : second) {
                boolean latClose = doubleCloseEnough(f.lat(), s.lat());
                boolean lngClose = doubleCloseEnough(f.lng(), s.lng());
                isMatchingPoint = latClose && lngClose;
                if (isMatchingPoint) {
                    break;
                }
            }
            if (!isMatchingPoint) {
                return false;
            }
        }
        return true;
    }

    public static <T> void doThreads(Function<T, Runnable> runnableProducer, Iterable<T> inputs) {
        List<Thread> threads = new ArrayList<>();

        for (T input : inputs) {
            threads.add(new Thread(runnableProducer.apply(input)));
        }

        startAndJoin(threads);
    }

    public static void doThreads(Supplier<Runnable> runnableSupplier, int amount) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            threads.add(new Thread(runnableSupplier.get()));
        }

        startAndJoin(threads);
    }

    public static void startAndJoin(Iterable<Thread> threads) {
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) { }
        }
    }

}
