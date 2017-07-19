package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.factory.TestFixtureFactory;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Armin Naderi.
 */
abstract class StatisticResolverTest {

    StatisticResolver resolver;

    @BeforeEach
    void beforeEachTest() {
        HandHistory handHistory = TestFixtureFactory.createValidInstance(HandHistory.class);
        replaceHandHistory(handHistory);
    }

    abstract void replaceHandHistory(HandHistory handHistory);
}
