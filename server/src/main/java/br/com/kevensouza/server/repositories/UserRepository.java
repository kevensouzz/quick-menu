package br.com.kevensouza.server.repositories;

import br.com.kevensouza.server.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserDetails findByUsername(String username);
    UserDetails findByEmail(String email);
}
