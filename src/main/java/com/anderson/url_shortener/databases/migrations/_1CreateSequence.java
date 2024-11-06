package com.anderson.url_shortener.databases.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import com.anderson.url_shortener.entities.SequenceEntity;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

@ChangeLog(order = "1")
public class _1CreateSequence {
    
    @ChangeSet(order = "1", author = "", id = "createSequenceCollection")
    public void createSequenceCollection(MongoTemplate mongoTemplate){
        if (! mongoTemplate.collectionExists("sequences")) {
            mongoTemplate.createCollection("sequences");
        }
    }

    @ChangeSet(order = "1", author = "", id = "addCollectionSequences")
    public void addCollectionSequences(MongoTemplate mongoTemplate) {
        SequenceEntity sequenceEntity = new SequenceEntity("users", 1);
        SequenceEntity sequenceEntity2 = new SequenceEntity("urls", 1);

        mongoTemplate.insert(sequenceEntity, "sequences");
        mongoTemplate.insert(sequenceEntity2, "sequences");
    }
}
