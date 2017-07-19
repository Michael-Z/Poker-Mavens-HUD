package com.chronpwn.pokermavenshud.repository;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Armin Naderi.
 */
@Repository
public interface HandHistoryRepository extends JpaRepository<HandHistory, Long> {
}
