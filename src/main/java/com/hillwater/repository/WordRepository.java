package com.hillwater.repository;

import com.hillwater.domain.Word;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Word entity.
 */
@SuppressWarnings("unused")
public interface WordRepository extends JpaRepository<Word,Long> {

}
