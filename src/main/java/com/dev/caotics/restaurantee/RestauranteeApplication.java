package com.dev.caotics.restaurantee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class RestauranteeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestauranteeApplication.class, args);
		System.out.println("\n\nAPPLICATION IS ONLINE !\n\n");
	}
}
