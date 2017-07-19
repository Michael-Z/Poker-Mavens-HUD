package com.chronpwn.pokermavenshud.repository;

import com.chronpwn.pokermavenshud.domain.PokerAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Armin Naderi.
 */
@Repository
public interface PokerActionRepository extends JpaRepository<PokerAction, Long> {
}
