package com.chronpwn.pokermavenshud.factory;

/**
 * @author Armin Naderi.
 */
public interface TestFixtureCreator<T> {

    /**
     * Does satisfy the JSR 303 / Hibernate annotations.
     */
    T validInstance();
    /**
     * Does not satisfy the JSR 303 / Hibernate annotations.
     */
    T invalidInstance();
    /**
     * Does not have any transitive dependencies populated.
     */
    T lazyInstance();
    boolean supports(Class<?> type);
}
