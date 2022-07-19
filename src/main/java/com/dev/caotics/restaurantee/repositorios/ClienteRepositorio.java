package com.dev.caotics.restaurantee.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.caotics.restaurantee.models.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	Cliente findByEmail(String email);
}
