package com.smartcontact.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MyConfig  extends WebSecurityConfigurerAdapter{
	
	@Bean
	public UserDetailsService getUserDetailsService()
	{
		return new UserDetailsImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return  new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticatorProvider()
	{
		
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		 daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		 
		 return daoAuthenticationProvider;
	}
	
	
	//Configure method in ..
	/*
	 * from this method simple we are using which type of authentication we are
	 * going to provide wheter it is dao or in memory
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
		auth.authenticationProvider(authenticatorProvider());
		
		
	}
	
	/* from this simple we are giving which route we have to protect */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll().and().
		formLogin().loginPage("/signin")
		.loginProcessingUrl("/dologin")
		.defaultSuccessUrl("/user/index").
		and()
		.csrf()
		.disable(); 
		
		
		
	
	}
	
	
	
	

	
	
	
	
	

}
