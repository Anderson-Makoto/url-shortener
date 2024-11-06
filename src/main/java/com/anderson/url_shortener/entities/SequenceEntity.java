package com.anderson.url_shortener.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "sequences")
@Getter
@Setter
public class SequenceEntity {
    private String entity;
    private Integer id_sequence;

    public SequenceEntity(String entity, Integer id_sequence) {
        this.entity = entity;
        this.id_sequence = id_sequence;
    }
}
