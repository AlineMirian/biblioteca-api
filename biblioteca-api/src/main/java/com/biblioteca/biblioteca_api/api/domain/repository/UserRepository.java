package com.biblioteca.biblioteca_api.api.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.biblioteca_api.api.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByCpf(String cpf);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
}
