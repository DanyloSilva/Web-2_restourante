package com.dev.caotics.restaurantee.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.caotics.restaurantee.models.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

}
