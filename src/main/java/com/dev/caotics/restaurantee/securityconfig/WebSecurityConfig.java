package com.dev.caotics.restaurantee.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration

public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserDetailsServiceBuscador userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.httpBasic()
		.and()
		.authorizeHttpRequests()
		.antMatchers("/meusPedidos").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")
		.antMatchers("/index").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")
		.antMatchers("/").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")
		.antMatchers("/cadastroDePrato").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/cadastrarFormaDePagamento").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/formasDePagamentoLista").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/listaDeClientes").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/atribuirPapeis/**").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/listaDePedidos").hasAnyAuthority("ROLE_ADMIN")
		.antMatchers("/pedirPrato/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/paginaLoginPersonalizada").permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/paginaLoginPersonalizada").permitAll()
		.and()
		.rememberMe().userDetailsService(userDetailsService)
		.and()
		.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
