package com.chronpwn.pokermavenshud.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Armin Naderi.
 */
public class TestFixtureFactory {

    private static List<TestFixtureCreator<?>> testFixtureCreators;

    static {
        testFixtureCreators = loadTestFixtureCreators();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createValidInstance(Class<T> type) {
        for (TestFixtureCreator<?> creator : testFixtureCreators) {
            if (creator.supports(type)) {
                // Guaranteed to be an instance of T
                return (T) creator.validInstance();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInvalidInstance(Class<T> type) {
        for (TestFixtureCreator<?> creator : testFixtureCreators) {
            if (creator.supports(type)) {
                // Guaranteed to be an instance of T
                return (T) creator.invalidInstance();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createLazyInstance(Class<T> type) {
        for (TestFixtureCreator<?> creator : testFixtureCreators) {
            if (creator.supports(type)) {
                // Guaranteed to be an instance of T
                return (T) creator.lazyInstance();
            }
        }
        return null;
    }

    private static List<TestFixtureCreator<?>> loadTestFixtureCreators() {
        List<TestFixtureCreator<?>> testFixtureCreators = new ArrayList<>();

        Iterator<TestFixtureCreator> creatorIterator = ServiceLoader.load(
                TestFixtureCreator.class,
                TestFixtureFactory.class.getClassLoader()
        ).iterator();

        while ( creatorIterator.hasNext() ) {
            TestFixtureCreator<?> testFixtureCreator = creatorIterator.next();
            testFixtureCreators.add(testFixtureCreator);
        }

        return testFixtureCreators;
    }
}
