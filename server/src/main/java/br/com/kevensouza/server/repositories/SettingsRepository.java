package br.com.kevensouza.server.repositories;

import br.com.kevensouza.server.models.SettingsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SettingsRepository extends JpaRepository<SettingsModel, UUID> {
}
