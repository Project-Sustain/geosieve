package sustain.geosieve.create;

import sustain.geosieve.create.uniform.GridWorker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
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

    public static <E> List<E> concat(List<E> first, List<E> second) {
        List<E> catted = new ArrayList<>(first.size() + second.size());
        for (int i = 0; i < first.size() + second.size(); i++) {
            catted.add((i < first.size()) ? first.get(i) : second.get(i - first.size()));
        }
        return catted;
    }

    public static <E> List<E> map(List<E> list, UnaryOperator<E> op) {
        List<E> mapped = new ArrayList<>(list.size());
        for (E e : list) {
            mapped.add(op.apply(e));
        }
        return mapped;
    }

    public static boolean coercibleToDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean coercibleToInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static <T, U> boolean biPredicateNoop(T t, U u) {
        return true;
    }

    public static <T> void doThreads(Function<T, Runnable> runnableProducer, Iterable<T> inputs) {
        List<Thread> threads = new ArrayList<>();

        for (T input : inputs) {
            threads.add(new Thread(runnableProducer.apply(input)));
        }

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
