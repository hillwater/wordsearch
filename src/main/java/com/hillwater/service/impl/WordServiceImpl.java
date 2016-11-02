package com.hillwater.service.impl;

import com.hillwater.service.WordService;
import com.hillwater.domain.Word;
import com.hillwater.repository.WordRepository;
import com.hillwater.repository.search.WordSearchRepository;
import com.hillwater.service.dto.WordDTO;
import com.hillwater.service.mapper.WordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Word.
 */
@Service
@Transactional
public class WordServiceImpl implements WordService{

    private final Logger log = LoggerFactory.getLogger(WordServiceImpl.class);
    
    @Inject
    private WordRepository wordRepository;

    @Inject
    private WordMapper wordMapper;

    @Inject
    private WordSearchRepository wordSearchRepository;

    /**
     * Save a word.
     *
     * @param wordDTO the entity to save
     * @return the persisted entity
     */
    public WordDTO save(WordDTO wordDTO) {
        log.debug("Request to save Word : {}", wordDTO);
        Word word = wordMapper.wordDTOToWord(wordDTO);
        word = wordRepository.save(word);
        WordDTO result = wordMapper.wordToWordDTO(word);
        wordSearchRepository.save(word);
        return result;
    }

    /**
     *  Get all the words.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Words");
        Page<Word> result = wordRepository.findAll(pageable);
        return result.map(word -> wordMapper.wordToWordDTO(word));
    }

    /**
     *  Get one word by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WordDTO findOne(Long id) {
        log.debug("Request to get Word : {}", id);
        Word word = wordRepository.findOne(id);
        WordDTO wordDTO = wordMapper.wordToWordDTO(word);
        return wordDTO;
    }

    /**
     *  Delete the  word by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Word : {}", id);
        wordRepository.delete(id);
        wordSearchRepository.delete(id);
    }

    /**
     * Search for the word corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WordDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Words for query {}", query);
        Page<Word> result = wordSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(word -> wordMapper.wordToWordDTO(word));
    }
}
