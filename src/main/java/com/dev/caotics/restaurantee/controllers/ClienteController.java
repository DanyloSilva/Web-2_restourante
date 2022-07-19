package com.dev.caotics.restaurantee.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.caotics.restaurantee.models.Cliente;
import com.dev.caotics.restaurantee.models.FormaPagamento;
import com.dev.caotics.restaurantee.models.Papeis;
import com.dev.caotics.restaurantee.models.Pedido;
import com.dev.caotics.restaurantee.models.Pratos;
import com.dev.caotics.restaurantee.repositorios.ClienteRepositorio;
import com.dev.caotics.restaurantee.repositorios.FormaPagamentoRepositorio;
import com.dev.caotics.restaurantee.repositorios.PapeisRepositorio;
import com.dev.caotics.restaurantee.repositorios.PedidoRepositorio;
import com.dev.caotics.restaurantee.repositorios.PratosRepositorio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@Controller
public class ClienteController {
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private FormaPagamentoRepositorio formaPagamentoRepositorio;
	
	@Autowired
	private PapeisRepositorio papeisRepositorio;
	
	@Autowired
	private PedidoRepositorio pedidoRepositorio;
	
	@Autowired
	private PratosRepositorio pratosRepositorio;
	
	@Autowired
	private PasswordEncoder criptografia;
	
	@RequestMapping(value="/cadastro", method=RequestMethod.GET)
	public String cadastroGET(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "cadastroForm";
	}
	
	@RequestMapping(value="/cadastro", method=RequestMethod.POST)
	public String cadastroPOST(@Valid Cliente cliente, BindingResult result) {
		if(result.hasErrors()) {
			return "cadastroForm";
		}
		String passwordEncoder = criptografia.encode(cliente.getSenha());
		cliente.setSenha(passwordEncoder);
		Papeis papel = papeisRepositorio.findByPapel("ROLE_CLIENTE");
		List<Papeis> papeis = new ArrayList<Papeis>();
		papeis.add(papel);
		cliente.setPapeis(papeis);
		clienteRepositorio.save(cliente);
		return "redirect:/paginaLoginPersonalizada";
	}
	
	@RequestMapping("/meusPedidos")
	public String retornarPedidosDoCliente(Model model, @CurrentSecurityContext(expression = "authentication.name") String login) {
		Cliente cliente = clienteRepositorio.findByEmail(login);
		model.addAttribute("clienteLogado", cliente);
		model.addAttribute("listaDePratos", pratosRepositorio.findAll());
		return "index";
	}
	
	@RequestMapping(value="/cadastroDePrato", method=RequestMethod.GET)
	public String cadastroPratoGET(Model model) {
		model.addAttribute("prato", new Pratos());
		return "cadastroDePrato";
	}
	
	@RequestMapping(value="/cadastroDePrato", method=RequestMethod.POST)
	public String cadastroPratoPOST(Model model, Pratos prato) {
		pratosRepositorio.save(prato);
		return "redirect:/index";
	}
	
	@RequestMapping(value="/cadastrarFormaDePagamento", method=RequestMethod.GET)
	public String cadastroFormaDePagamentoGET(Model model) {
		model.addAttribute("formaDePagamento", new FormaPagamento());
		return "cadastroFormaDePagamento";
	}
	
	@RequestMapping(value="/cadastrarFormaDePagamento", method=RequestMethod.POST)
	public String cadastroFormaDePagamentoPOST(Model model, FormaPagamento formaPagamento) {
		formaPagamentoRepositorio.save(formaPagamento);
		return "redirect:/formasDePagamentoLista";
	}
	
	@RequestMapping(value="/formasDePagamentoLista", method=RequestMethod.GET)
	public String formasDePagamentoLISTA(Model model, FormaPagamento formaPagamento) {
		model.addAttribute("formasDePagamentos", formaPagamentoRepositorio.findAll());
		return "formasDePagamentoLista";
	}
	
	@RequestMapping(value="/listaDeClientes", method=RequestMethod.GET)
	public String clientesLISTA(Model model, Cliente clientes) {
		model.addAttribute("listaDeClientes", clienteRepositorio.findAll());
		return "clientesLista";
	}
	
	@RequestMapping(value="/atribuirPapeis/{id}", method=RequestMethod.GET)
	public String atribuirPapeisGET(@PathVariable("id") Long id, Model model) {
		Optional<Cliente> cliente = clienteRepositorio.findById(id);
		if(!cliente.isPresent()) {
			throw new IllegalArgumentException("Usuário não encontrado!");
		}
		Cliente clienteFound = cliente.get();
		model.addAttribute("cliente", clienteFound);
		model.addAttribute("papeisLista", papeisRepositorio.findAll());
		return "formularioEditarRoles";
	}
	
	@RequestMapping(value="/atribuirPapeis/{id}", method=RequestMethod.POST)
	public String atribuirPapeisPOST(@RequestParam(value="checkBoxSelecionados", required=false) int[] papeisIndices, Model model, Cliente cliente, @PathVariable("id") Long id, RedirectAttributes attributes) {
		model.addAttribute("listaDeClientes", clienteRepositorio.findAll());
		if(papeisIndices==null) {
			cliente.setId(id);
			attributes.addFlashAttribute("mensagem", "Ao menos uma permissão deve ser marcada!");
			return "redirect:/atribuirPapeis/"+id;
		} else {
			List<Papeis> papeis = new ArrayList<Papeis>();
			for(int i = 0; i < papeisIndices.length; i++) {
				long idPapel = papeisIndices[i];
				Optional<Papeis> papelOptional = papeisRepositorio.findById(idPapel);
				if(papelOptional.isPresent()) {
					Papeis papel = papelOptional.get();
					papeis.add(papel);
					}
			}
			Optional<Cliente> clienteOptional = clienteRepositorio.findById(id);
			if(clienteOptional.isPresent()) {
				Cliente client = clienteOptional.get();
				client.setPapeis(papeis);
				clienteRepositorio.save(client);
			}
		}
		return "redirect:/listaDeClientes";
	}
	
	@RequestMapping(value="/listaDePedidos", method=RequestMethod.GET)
	public String pedidosTotaisLISTA(Model model) {
		model.addAttribute("pedidos", pedidoRepositorio.findAll());
		return "listaDePedidos";
	}
	
	@RequestMapping(value="/pedirPrato/{id}", method=RequestMethod.GET)
	public String pedirPratoGET(Model model, @PathVariable("id") Long id){
		Optional<Pratos> prato = pratosRepositorio.findById(id);
		if(!prato.isPresent()) {
			throw new IllegalArgumentException("Prato não encontrado!");
		}
		Pratos pratoFound = prato.get();
		model.addAttribute("prato", pratoFound);
		model.addAttribute("formasDePagamento", formaPagamentoRepositorio.findAll());
		model.addAttribute("pedido", new Pedido());
		return "formularioRealizarPedido";
	}
	
	@PostMapping(value="/pedirPrato/{id}")
	public String pedirPratoPOST(Model model, @RequestParam(value="radioIdSelected") Long radioIdSelectd, @PathVariable("id") Long id, @RequestParam(value="descricaoPedido") String descricaoPedido, @CurrentSecurityContext(expression = "authentication.name") String login, RedirectAttributes attributes) {
		Pedido pedido = new Pedido();
		Optional<Pratos> pratoOptional = pratosRepositorio.findById(id);
		if(pratoOptional.isPresent()) {
			Pratos pratoFound = pratoOptional.get();
			pedido.setPrato(pratoFound);
			pedido.setPreco(pratoFound.getPreco());
		} else {
			attributes.addFlashAttribute("mensagem", "Prato não encontrado!");
		}
		Cliente clienteautenticado = clienteRepositorio.findByEmail(login);
		Optional<FormaPagamento> formaPagamentoOptional = formaPagamentoRepositorio.findById(radioIdSelectd);
		if(formaPagamentoOptional.isPresent()) {
			FormaPagamento formaPagamentoFound = formaPagamentoOptional.get();
			pedido.setFormaPagamento(formaPagamentoFound);
		} else {
			attributes.addFlashAttribute("mensagem", "Forma de Pagamento inválida!");
		}
		pedido.setCliente(clienteautenticado);
		pedido.setDescricao(descricaoPedido);
		pedido.setHoraDoPedido(LocalDateTime.now());
		pedidoRepositorio.save(pedido);
		return "redirect:/meusPedidos";
	}
	
}
