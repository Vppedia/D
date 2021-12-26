package com.smartcontact.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcontact.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{

	//Query to fetch data from contact based on User id
	
	@Query("from Contact as c where c.user.id=:userId")
	//Pageable-current page
	//contacts-per page-5
	 public Page<Contact> getUserByUserId(@Param("userId") int userId,Pageable  pageable);
	
	
	@Query("from Contact as c where c.cId=:cId")
	//Pageable-current page
	//contacts-per page-5
	 public Contact getUserByCId(@Param("cId") int cId);



}
