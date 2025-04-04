package com.quotepro;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class QuoteproApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		 // Load the .env file
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.configure()
                           .ignoreIfMissing() // Ignore the error if .env is missing
                           .load();
        } catch (Exception e) {
            System.out.println("No .env file found. Using system environment variables.");
        }
        
       
		SpringApplication.run(QuoteproApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QuoteproApplication.class);
	}
}
