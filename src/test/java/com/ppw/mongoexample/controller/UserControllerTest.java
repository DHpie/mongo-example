package com.ppw.mongoexample.controller;

import com.ppw.mongoexample.model.User;
import com.ppw.mongoexample.mongoexample.MongoExampleApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.junit.Assert.*;

public class UserControllerTest  extends MongoExampleApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void getAndOr() {
        Query query = new Query();
        Criteria criteria =new Criteria();

        criteria.andOperator(Criteria.where("userName").is("djl"),Criteria.where("age").is(50))
                .orOperator(Criteria.where("email").regex(".*?" +"88"+ ".*"),Criteria.where("password").regex(".*?" +"he"+ ".*"));
        query.addCriteria(criteria);
        List<User> result = mongoTemplate.find(query, User.class, "user");
        System.out.println("query: " + query + " | orQuery: " + result);
    }
}