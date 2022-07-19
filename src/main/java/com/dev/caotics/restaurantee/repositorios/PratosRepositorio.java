package com.dev.caotics.restaurantee.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.caotics.restaurantee.models.Pratos;

public interface PratosRepositorio extends JpaRepository<Pratos, Long> {

}
