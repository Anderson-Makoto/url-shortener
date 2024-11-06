package com.anderson.url_shortener.databases.migrations;

import org.springframework.data.mongodb.core.MongoTemplate;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

@ChangeLog(order = "2")
public class _2CreateUsers {
    
    @ChangeSet(order = "1", author = "", id = "createUsersCollection")
    public void createUsersCollection(MongoTemplate mongoTemplate) {
        if (! mongoTemplate.collectionExists("users")) {
            mongoTemplate.createCollection("users");
        }
    }
}
