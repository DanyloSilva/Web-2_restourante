package com.dev.caotics.restaurantee.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.caotics.restaurantee.models.Papeis;

public interface PapeisRepositorio extends JpaRepository<Papeis, Long> {
	Papeis findByPapel(String papel);
}
