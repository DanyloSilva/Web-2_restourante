package com.dev.caotics.restaurantee.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dev.caotics.restaurantee.models.Cliente;
import com.dev.caotics.restaurantee.repositorios.ClienteRepositorio;

@Service
public class UserDetailsServiceBuscador implements UserDetailsService{
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Cliente cliente = clienteRepositorio.findByEmail(username);
		if(cliente != null) {
			return new User(cliente.getEmail(), cliente.getSenha(), true, true, true, true, cliente.getAuthorities());
		} else {
			throw new UsernameNotFoundException("falha ao encontrar usuario");
		}
	}
	
}
