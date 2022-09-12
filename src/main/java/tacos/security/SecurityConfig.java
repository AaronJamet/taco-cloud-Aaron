package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tacos.data.UserRepository;
import tacos.domain.Users;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
				// The access() method allows the use of SpEL (Spring Expression Language)
				// to allow declaring richer and more customized security rules
				.antMatchers("/design", "/orders").access("hasRole('USER')") 
				.antMatchers("/**").access("permitAll")
				 
			.and()
				.formLogin()
					// Specifies the path to find the CUSTOM login page
					.loginPage("/login")
					// Specify URL to which user will be redirected if the login is successful
					.defaultSuccessUrl("/design", true)
					
			.and()
				.logout()
					.logoutSuccessUrl("/");
	}

	@Bean
	public PasswordEncoder encoder() {
		// Applies bcrypt strong hashing encryption when saves the passwords in the BBDD
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}
	
}
