package com.ppw.mongoexample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.ppw.mongoexample.model.User;
import com.ppw.mongoexample.repository.UserRepository;

@RestController
@RequestMapping("/v1/user")
public class UserController {

   @Autowired
   private UserRepository userRepository;
   @Autowired
   MongoTemplate mongoTemplate;


   @ResponseStatus(HttpStatus.CREATED)
   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
   public User createUser(@RequestBody User user) {
      return userRepository.save(user);
   }
   
   /**
    * 根据id查询
    * @param id
    * @return
    */
   @GetMapping(value="/{id}")
   public User readUserById(@PathVariable("id") String id){
      return userRepository.findOne(id);
   }
   
   /**
    * 根据一个或者多个属性查询单个结果
    * @param name
    * @return
    */
   @GetMapping(value="/name/{name}")
   public User readUserByName(@PathVariable("name") String name){
      User user = new User();
      user.setUserName(name);
      ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age","createTime");
      Example<User> example = Example.of(user, matcher);
      return userRepository.findOne(example);
   }


   @GetMapping(value="/list/{name}/em/{email}")
   public List<User> readAllUser(@PathVariable("name") String name,@PathVariable("email") String email){
      User user = new User();
      user.setUserName(name);
      user.setEmail(email);
      ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age","createTime");
      Example<User> example = Example.of(user, matcher);
      return userRepository.findAll(example);
   }


   /**
    * 根据一个或者多个属性分页查询 
    * @param pageNumber
    * @param pageSize
    * @return
    */
   @GetMapping(value = "/page/{pageNumber}/pagesize/{pageSize}/name/{name}")
   public Page<User> readUsersByPage(@PathVariable("pageNumber") int pageNumber,
         @PathVariable("pageSize") int pageSize,@PathVariable("name") String name) {
      User user = new User();
      user.setUserName(name);
      ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age","createTime");
      Example<User> example = Example.of(user, matcher);
      if (pageNumber < 1) {
         pageNumber = 1;
      } else if (pageSize == 0) {
         pageSize = 20;
      }
      
      PageRequest pageable = new PageRequest(pageNumber - 1, pageSize);
      return userRepository.findAll(example, pageable);
   }
   
   /**
    * 根据用户年龄升序排序
    * @return
    */
   @GetMapping
   public List<User> readUsers(){
      
      Order order = new Order(Direction.ASC,"age");
      Sort sort = new Sort(order);
      return userRepository.findAll(sort);
   }
   
   /**
    * 模糊查询带分页
    * @param pageNumber
    * @param pageSize
    * @param keyWords
    * @return
    */
   @GetMapping(value = "/page/{pageNumber}/pagesize/{pageSize}/keyword/{keyWords}")
   public Page<User> readUsersByKeywords(@PathVariable("pageNumber") int pageNumber,
         @PathVariable("pageSize") int pageSize,@PathVariable("keyWords") String keyWords) {
      if (keyWords == null) {
         keyWords = "";
      }
      if (pageNumber < 1) {
         pageNumber = 1;
      } else if (pageSize == 0) {
         pageSize = 20;
      }
      PageRequest pageable = new PageRequest(pageNumber - 1, pageSize);
      return userRepository.findByUserNameLike(keyWords, pageable);
   }
   
   @ResponseStatus(HttpStatus.OK)
   @DeleteMapping(value="/{id}")
   public void removeUser(@PathVariable("id") String id) {
      userRepository.delete(id);
   }


   /**
    * 利用mongoTemplate进行复杂查询
    * @param user
    * @return
    */
   @GetMapping(value="/list/find")
   public List<User> findByMultiple(@ModelAttribute("user") User user){
      Query query = new Query();
      Criteria criteria =new Criteria();

      if(!StringUtils.isEmpty(user.getUserName())){
         query.addCriteria(Criteria.where("userName").is(user.getUserName()));
      }
      if(user.getAge()!=null){
         query.addCriteria(Criteria.where("age").is(user.getAge()));
      }

      if(!StringUtils.isEmpty(user.getEmail())){
         query.addCriteria(criteria.orOperator(Criteria.where("email").regex(".*?" +user.getEmail()+ ".*"),Criteria.where("password").regex(".*?" +user.getEmail()+ ".*")));
      }
      List<User> result = mongoTemplate.find(query, User.class, "user");
      System.out.println("query: " + query + " | orQuery: " + result);
      return result;

      //复杂sql的转化(in/and/or/大于/小于/等于)
      //select * from mongoTest where valueStr in ("nihao","dajiahao")
      // and valueInt>=2 and （valueInt2 =15 or time>= (now -interval 5 day))
//      Query query2 = new Query();
//      Criteria criteria =new Criteria();
//      List<String> valueStrParam = new ArrayList<>();
//      valueStrParam.add("nihao");
//      valueStrParam.add("dajiahao");
//      criteria.and("valueStr").in(valueStrParam).andOperator(Criteria.where("valueInt").gte(2)).
//              orOperator(Criteria.where("valueInt2").is(15), Criteria.where("time")
//                      .gte(LocalDateTime.now().minus(5, ChronoUnit.DAYS)));
//      query2.addCriteria(criteria);
//      List<MongoTestEntity> entityList1 = mongoTemplate.find(query2, MongoTestEntity.class, "mongoTest");
//      System.out.println(entityList1);
   }



}
