package com.kucukcinar.repositories;

import com.kucukcinar.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Doc repository.
 */
@Repository
public interface FileRepository extends JpaRepository<File,Integer>{

}