package com.smartcontact.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcontact.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
	
	
	@Query("Select u from User u where u.email=:email")
	public User getUserByName(@Param("email") String email);

}
