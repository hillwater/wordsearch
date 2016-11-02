package com.hillwater.repository.search;

import com.hillwater.domain.Word;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Word entity.
 */
public interface WordSearchRepository extends ElasticsearchRepository<Word, Long> {
}
