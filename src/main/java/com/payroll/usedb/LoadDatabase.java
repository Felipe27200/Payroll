package com.payroll.usedb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payroll.entity.Employee;
import com.payroll.repository.EmployeeRepository;


@Configuration
public class LoadDatabase 
{
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
	
	/*
	 * Spring boot correrá todos los beans CommandLineRunner una 
	 * vez el contexto de la aplicación es cargado.
	 * */
	@Bean
	/*
	 * Solicitará una copia del EmployeeRepository
	 * */
	CommandLineRunner initDatabase(EmployeeRepository repository)
	{
		
		return args -> {
			log.info("Preloading: " + repository.save(new Employee("Frodo Baggins", "thief")));
			log.info("Preloading: " + repository.save(new Employee("Frodo Baggins", "thief")));
		};
	}
}
