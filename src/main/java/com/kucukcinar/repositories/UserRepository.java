package com.kucukcinar.repositories;

import com.kucukcinar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM users u WHERE u.email = :email",nativeQuery = true)
    Optional<User> findByEmail(String email);
}
