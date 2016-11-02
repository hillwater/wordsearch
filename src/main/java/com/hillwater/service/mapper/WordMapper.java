package com.hillwater.service.mapper;

import com.hillwater.domain.*;
import com.hillwater.service.dto.WordDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Word and its DTO WordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WordMapper {

    WordDTO wordToWordDTO(Word word);

    List<WordDTO> wordsToWordDTOs(List<Word> words);

    Word wordDTOToWord(WordDTO wordDTO);

    List<Word> wordDTOsToWords(List<WordDTO> wordDTOs);
}
