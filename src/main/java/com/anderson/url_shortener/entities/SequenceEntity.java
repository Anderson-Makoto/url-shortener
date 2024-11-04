package com.anderson.url_shortener.entities;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "sequences")
@Getter
@Setter
@AllArgsConstructor
public class SequenceEntity {
    private String entity;
    private String id_sequence;
}
