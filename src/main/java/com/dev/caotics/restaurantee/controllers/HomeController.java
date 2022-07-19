package com.dev.caotics.restaurantee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dev.caotics.restaurantee.models.Cliente;
import com.dev.caotics.restaurantee.repositorios.ClienteRepositorio;
import com.dev.caotics.restaurantee.repositorios.PedidoRepositorio;
import com.dev.caotics.restaurantee.repositorios.PratosRepositorio;

@Controller
public class HomeController {
	
	@Autowired
	private PedidoRepositorio pedidoRepositorio;
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private PratosRepositorio pratosRepositorio;
	
	@RequestMapping("/") 
	public String index(Model model, @CurrentSecurityContext(expression = "authentication.name") String login) {
		Cliente cliente = clienteRepositorio.findByEmail(login);
		model.addAttribute("clienteLogado", cliente);
		model.addAttribute("listaDePratos", pratosRepositorio.findAll());
		return "index";
	}
	@RequestMapping("/index") 
	public String index2(Model model, @CurrentSecurityContext(expression = "authentication.name") String login) {
		Cliente cliente = clienteRepositorio.findByEmail(login);
		model.addAttribute("clienteLogado", cliente);
		model.addAttribute("listaDePratos", pratosRepositorio.findAll());
		return "index";
	}
	
	@RequestMapping(value="/paginaLoginPersonalizada", method=RequestMethod.GET)
	public String paginaDeLogin(Model model) {
		return "paginaLoginPersonalizada";
	}

}
