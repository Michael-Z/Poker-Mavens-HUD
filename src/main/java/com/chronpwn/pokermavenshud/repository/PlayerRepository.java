package com.chronpwn.pokermavenshud.repository;

import com.chronpwn.pokermavenshud.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Armin Naderi.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUsername(String username);
}
