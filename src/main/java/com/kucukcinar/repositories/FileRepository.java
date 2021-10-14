package com.kucukcinar.repositories;

import com.kucukcinar.entities.File;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * The interface Doc repository.
 */
@Repository
public interface FileRepository extends JpaRepository<File,Integer>{

    /**
     * Find by name optional.
     *
     * @param filename the filename
     * @return the optional
     */
    @Query(value = "SELECT * FROM files f WHERE f.file_name = :filename",nativeQuery = true)
    Optional<File> findByName(String filename);
}