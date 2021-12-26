package com.smartcontact.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entity.User;
import com.smartcontact.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	UserRepository UserRepository;
	
	@GetMapping("/")
	public String home(Model m)
	{
	   m.addAttribute("title","Home-SmartContactManager");
	   System.out.println("Inside home method");	
	
	    return "home";
	}
	
	@GetMapping("/about")
	public String about(Model m)
	{
	   m.addAttribute("title","About-SmartContactManager");
	   System.out.println("Inside about method");	
	
	    return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model m)
	{
	  m.addAttribute("title","signup-SmartContactManager");
	  m.addAttribute("user",new User());
	   System.out.println("Inside signup method");	
	
	    return "signup";
	}
	
	/* Processing the form data for registration */
	
	@PostMapping("/do_register")
	public String doResgister(@ModelAttribute("user") User user,
			@RequestParam(value="agreement",defaultValue="false")boolean agreement,
			Model model,HttpSession session) 
	{
		 
		try {
			
			 System.out.println("Inside doRegister Method!!!!");
			 
			 if(!agreement)
			 {
				throw new Exception("You have not agreed to terms and conditions");
				 
				 
			 }
			 
			 System.out.println("agreement "+agreement);
			 
			 System.out.println("UserName "+user.getName());
			 System.out.println("User Email "+user.getEmail());
			 System.out.println("User Password "+user.getPassword());
			 System.out.println("User About "+user.getAbout());
			 
			 user.setRole("ROLE_USER");
			 user.setEnabled(true);
			 user.setImageUrl("default.png");
			 
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			 
			 
			 
			model.addAttribute(user);
			 
			 //for saving the data into the database
			 this.UserRepository.save(user);
			 
			 
			     model.addAttribute("user",new User());
				 session.setAttribute("message", new Message("Successful Registered!! ","alert-success"));
			 
				 return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(user);
			session.setAttribute("message", new Message("Something went wrong!! "+e.getMessage(),"alert-danger"));
			return "signup";
		}
	
	    
	}
  
	
	//handling Login Form
	@GetMapping("/signin")
	public String handLoginPage(Model m)
	{
		m.addAttribute("title","Login-SmartContactManager");
		return "login";
	}
	

}
