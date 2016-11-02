package com.hillwater.service.impl;

import com.google.common.base.Joiner;
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Word.
 */
@Service
@Transactional
public class WordServiceImpl implements WordService{

    private final Logger log = LoggerFactory.getLogger(WordServiceImpl.class);

    private final static String DICTIONARY_PATH_KEY="dictionary-path";

    private final static String hyphen_spilitter = "——————————";

    // TODO: there is a bug: when word name has space
    private final static Pattern wordPattern = Pattern.compile(".*\\s+([a-zA-Z0-9-/]+)(\\s+.*)?$");

    @Inject
    private WordRepository wordRepository;

    @Inject
    private WordMapper wordMapper;

    @Inject
    private WordSearchRepository wordSearchRepository;


    @PostConstruct
    public void initData() {
        cleanData();
        insertDictionaryData();
    }


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

    private String getDictionaryFolderPath() {
        return System.getProperty(DICTIONARY_PATH_KEY, "/home/zhong_s/workspace/wordsearch/dictionary");
    }

    private void insertDictionaryData() {
        try {
            Files.list(Paths.get(getDictionaryFolderPath()))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt"))
                .forEach(p->insertOneFileData(p));
        } catch (IOException e) {
            throw new RuntimeException("list dictionary files failed.", e);
        }
    }

    private void cleanData() {
        wordSearchRepository.deleteAll();
        wordRepository.deleteAll();
    }

    private void insertOneFileData(Path file) {
        try (Stream<String> stream = Files.lines(file)) {

            String dictionaryName = getDictionaryName(file);



            List<String> lines = stream.map(line->line.trim()).collect(Collectors.toList());

            List<Word> wordList;

            if(lines.stream().anyMatch(line->line.contains(hyphen_spilitter))) {
                wordList = extractWordsByHyphenLine(dictionaryName, lines);
            }else {
                wordList = extractWordsByEmptyLine(lines);
            }


            wordList.stream().limit(5).forEach(System.out::println);

            wordRepository.save(wordList);
            wordSearchRepository.save(wordList);

        } catch (IOException e) {
            throw new RuntimeException("read dictionary file failed, file: "+file, e);
        }
    }

    private String getDictionaryName(Path file) {
        return file.getFileName().toString();
    }

    private List<Word> extractWordsByHyphenLine(String dictionaryName, List<String> lines) {
        List<String> tmpLines = new ArrayList<>();

        List<Word> result = new ArrayList<>();


        for(int i = 0; i<lines.size(); i++) {

            String line = lines.get(i);

            if(line.contains(hyphen_spilitter)) {
                if(tmpLines.size()<= 1) {
                    // for file begin
                    tmpLines.clear();
                }else {
                    tmpLines.remove(tmpLines.size()-1);

                    result.add(generateWordByData(dictionaryName, tmpLines));

                    tmpLines.clear();
                }
            }else {
                tmpLines.add(line);
            }

        }

        // for file end
        tmpLines.remove(tmpLines.size()-1);

        result.add(generateWordByData(dictionaryName, tmpLines));

        tmpLines.clear();

        return result;
    }

    private Word generateWordByData(String dictionaryName, List<String> content) {
        return new Word(null, extractWordName(content.get(0)), dictionaryName,
            Joiner.on("\n").join(content).trim().toString());
    }

    private String extractWordName(String line) {
        Matcher m = wordPattern.matcher(line);
        if (m.find()) {
            return m.group(1).trim();
        }else {
            return "";
        }
    }

    private List<Word> extractWordsByEmptyLine(List<String> lines) {
        return Collections.EMPTY_LIST;
    }

//    public static void main(String[] args) {
//        WordServiceImpl wordService = new WordServiceImpl();
//
//        wordService.initData();
//    }
}
