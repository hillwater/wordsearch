package com.hillwater.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Word.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "word")
@Document(indexName = "word")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @NotNull
    @Size(max = 200)
    @Column(name = "dictionary_name", length = 200, nullable = false)
    private String dictionaryName;

    @Field(searchAnalyzer = "ik", analyzer = "ik")
    @NotNull
    @Size(max = 100000)
    @Column(name = "explain", length = 100000, nullable = false)
    private String explain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Word name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public Word dictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
        return this;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public String getExplain() {
        return explain;
    }

    public Word explain(String explain) {
        this.explain = explain;
        return this;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Word word = (Word) o;
        if(word.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, word.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Word{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", dictionaryName='" + dictionaryName + "'" +
            ", explain='" + explain + "'" +
            '}';
    }
}
