package br.com.kevensouza.server.repositories;

import br.com.kevensouza.server.models.MenuModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<MenuModel, UUID> {
    Optional<MenuModel> findByCode(String code);
}
