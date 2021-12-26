package com.smartcontact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entity.User;

public class UserDetailsImpl implements UserDetailsService {
	
	@Autowired
    private UserRepository UserRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	
		User user = UserRepository.getUserByName(username);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("Could not found User with given UserName");
		
		}
		
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		
		return customUserDetails;
		
	}
}
