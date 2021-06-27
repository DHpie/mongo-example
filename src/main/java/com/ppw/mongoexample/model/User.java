package com.ppw.mongoexample.model;

public class User {
   
   private String id;
   private String userName;
   private String password;
   private String email;
   private Integer age;
   private long createTime;
   
   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public String getUserName() {
      return userName;
   }
   public void setUserName(String userName) {
      this.userName = userName;
   }
   public String getPassword() {
      return password;
   }
   public void setPassword(String password) {
      this.password = password;
   }
   public Integer getAge() {
      return age;
   }
   public void setAge(Integer age) {
      this.age = age;
   }
   public long getCreateTime() {
      return createTime;
   }
   public void setCreateTime(long createTime) {
      this.createTime = createTime;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }
}
