package com.smartcontact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.dao.ContactRepository;
import com.smartcontact.dao.UserRepository;
import com.smartcontact.entity.Contact;
import com.smartcontact.entity.User;
import com.smartcontact.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	ContactRepository contactRepository;
	
	//To add Common data to all the handler
	@ModelAttribute
	public void addCommonData(Model model ,Principal prince)
	{
		String name = prince.getName();
		System.out.println("UserName " + name);
		User user = userRepository.getUserByName(name);
		System.out.println(user);

		model.addAttribute("user", user);

	}
	
	@RequestMapping("/index")
	public String dashBoard(Model model,Principal prince)
	{     
		
		model.addAttribute("title","User-SmartContactManager");
		  return "/normal/user_dashBoard";
	}
	
	//add  contacts Handler
	
	@RequestMapping("/addContact")
	public String addContact(Model m)
	{
		m.addAttribute("title","AddContact-SmartContactManager");
		System.out.println("Inside addContact Method");
		
		 return "normal/add_contact";
	}
	
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact con,
			Principal principal,
			@RequestParam("profileImage") MultipartFile file,
			HttpSession session
			)
	{
		try {

			System.out.println("Inside process-contact Method");

			System.out.println("File Original Name " + file.getOriginalFilename());

			if (file.isEmpty()) {

				System.out.println(" User Do not choose any Image");
				con.setImage("default.png");

			} else {

				// upload the file into the folder and save the into the contact..

				con.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				System.out.println("File Uploaded Path " + path);

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("File Uploaded Successfullyy!!!!!!!");

			}

			String user = principal.getName();

			User loginUserName = this.userRepository.getUserByName(user);
			loginUserName.getContacts().add(con);

			con.setUser(loginUserName);

			this.userRepository.save(loginUserName);

			session.setAttribute("message", new Message("Your Contact is Added Successfully!! Add More..", "success"));

			System.out.println("Contact Added into Database Successfully!!!!!!!!");

			return "normal/add_contact";

		}
		 catch (Exception e) {
			 
			 session.setAttribute("message", new Message("Something went wrong !! Please try again","danger"));
			 
			 System.out.println(e);
			 e.printStackTrace();
			
		}
		 
		 return "normal/add_contact";
		  
	}
	
	//Show contacts handler mapper
	//show page=5[n]
	 
	@GetMapping("showContacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m,Principal principal)
	{

		System.out.println("Inside showContacts method ");
		m.addAttribute("title","View Contacts-SmartContactManager");

		String userName = principal.getName();
		User user = this.userRepository.getUserByName(userName);

		/* implementing pagination   */

		Pageable pageable = PageRequest.of(page, 2);

		Page<Contact> contacts = this.contactRepository.getUserByUserId(user.getId(),pageable);

		m.addAttribute("contacts",contacts);	
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());

		return "normal/show-contacts";
	}
	
	
	//Handler to show Particular details
	
	@GetMapping("{cId}/contact")
	public String showContactDetail(Model model,Principal principal,@PathVariable("cId") int cId)
	{
		model.addAttribute("title","showContactDetail-SmartContactManager");

		Contact userByCId = this.contactRepository.getUserByCId(cId);
		model.addAttribute("userDetail",userByCId);


		return "normal/showContactDetail";
	}
	
	
	//Handler mapper to delete Particular contact
	
	@GetMapping("delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cid,HttpSession session)
	{
		System.out.println("Inside deleteContact");
		Contact userByCId = this.contactRepository.getUserByCId(cid);
		
		this.contactRepository.delete(userByCId);
		
		System.out.println("contact Deleted");
		
		session.setAttribute("message", new Message("Contact Deleted Successfully..","success"));
		
		return "redirect:/user/showContacts/0";
	}
	
	
	//Handler Mapper to update contact
	@GetMapping("update/{cId}")
	public String update(Model model,@PathVariable("cId")Integer cId )
	{
		
		Contact contact = this.contactRepository.getUserByCId(cId);	
		model.addAttribute("title","update-Smart Contact Manager");
		model.addAttribute("contact",contact);
		
		return "normal/update-form";
	}
	
	//Handler Mapper to process update contact form
	 
	@PostMapping("process-update-contact")
	public String updateContact(@ModelAttribute Contact con,
			Principal principal,
			@RequestParam("profileImage") MultipartFile file,
			
			HttpSession session)
	
	{
		try {
			
			Contact oldContact = this.contactRepository.findById(con.getcId()).get();
			
			if(!file.isEmpty())
			{
				
				//rewrite the image
				
				
			
			
				
				
				
				
				//delete old Image
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				
		        File file1=new File(deleteFile,oldContact.getImage());
		        file1.delete();
		   
				
				
				//upload new Image
				
				
				// upload the file into the folder and save the into the contact..

				
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				con.setImage(file.getOriginalFilename());

				System.out.println("File Uploaded Successfullyy!!!!!!!");
				
			}
			else
			{
				System.out.println(" User Do not choose any new  Image");
				con.setImage(oldContact.getImage());
			}
			
			System.out.println("Inside updateContact Form  Contact !! " + con);

			String userName = principal.getName();

			User userByName = this.userRepository.getUserByName(userName);

			con.setUser(userByName);

			this.contactRepository.save(con);
			
			
			session.setAttribute("message", new Message("Your contact has been updated Successfully !!","success"));
			
			
			
			
			
		} catch (Exception e) {


		}
		
		
		
		return "redirect:/user/"+con.getcId()+"/contact";
	}
	
	
	//Handler to show profile 
	
	@GetMapping("/viewProfile")
	public String showProfile(Principal principal,Model m)
	{
		String name = principal.getName();
		 User user = this.userRepository.getUserByName(name);
		 m.addAttribute("user",user);
		
		return "normal/show-profile";
	}
	
	
	
}
