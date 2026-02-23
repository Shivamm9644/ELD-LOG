package com.mes.eld_log.security;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mes.eld_log.serviceImpl.UserInfoServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfing extends WebSecurityConfigurerAdapter{
	
	@Resource(name = "userInfoService")
//	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	@Bean
	public AuthenticationFilter authenticationTokenFilterBean() throws Exception {
		return new AuthenticationFilter();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests()
//				.anyRequest().authenticated()
//				.and().httpBasic()
//				.authenticationEntryPoint(authEntryPoint);
		http.cors().and().csrf().disable().authorizeRequests()
		.antMatchers("/auth/login").permitAll()
		
//				.antMatchers("/company/**").hasAnyRole("SUPERADMZIN")
//				.antMatchers("/users").permitAll()
//				.antMatchers("/download/dailyfeederreliability").permitAll()
//				.antMatchers("/download/**").permitAll()
//				.antMatchers("/dailylogsheet").permitAll()
//				.antMatchers("/scheduledcalls/**").permitAll()
		.antMatchers("/download/*").permitAll()		
		.antMatchers("/scheduledcalls/*").permitAll()
				// .antMatchers("/users").hasRole("ADMIN")
				//.anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(authEntryPoint)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("root").password("$2a$04$FlHdRJcVCeC2hzQTzPs7XOPkBMn8h99GstJ5WxkKBZd2mXDSliY0K").roles("USER");
	}
	
	@Bean
	public BCryptPasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
}
