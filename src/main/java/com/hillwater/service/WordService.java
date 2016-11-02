package com.hillwater.service;

import com.hillwater.service.dto.WordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Word.
 */
public interface WordService {

    /**
     * Save a word.
     *
     * @param wordDTO the entity to save
     * @return the persisted entity
     */
    WordDTO save(WordDTO wordDTO);

    /**
     *  Get all the words.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WordDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" word.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WordDTO findOne(Long id);

    /**
     *  Delete the "id" word.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the word corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WordDTO> search(String query, Pageable pageable);
}
