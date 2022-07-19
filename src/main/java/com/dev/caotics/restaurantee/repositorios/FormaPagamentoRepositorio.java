package com.dev.caotics.restaurantee.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.caotics.restaurantee.models.FormaPagamento;

public interface FormaPagamentoRepositorio extends JpaRepository<FormaPagamento, Long> {

}
