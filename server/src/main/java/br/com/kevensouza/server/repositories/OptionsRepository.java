package br.com.kevensouza.server.repositories;

import br.com.kevensouza.server.models.OptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OptionsRepository extends JpaRepository<OptionModel, UUID> {
}
