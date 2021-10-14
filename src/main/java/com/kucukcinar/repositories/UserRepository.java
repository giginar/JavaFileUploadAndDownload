package com.kucukcinar.repositories;

import com.kucukcinar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    @Query(value = "SELECT * FROM users u WHERE u.email = :email",nativeQuery = true)
    Optional<User> findByEmail(String email);
}
