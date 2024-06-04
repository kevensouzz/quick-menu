package br.com.kevensouza.server.repositories;

import br.com.kevensouza.server.models.EateryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EateryRepository extends JpaRepository<EateryModel, UUID> {
    boolean existsByName(String name);
}
