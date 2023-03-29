package com.payroll.usedb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payroll.entity.Employee;
import com.payroll.entity.Order;
import com.payroll.entity.Status;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.OrderRepository;


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
	CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository)
	{
		
		return args -> {
			log.info("Preloading: " + employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar")));
			log.info("Preloading: " + employeeRepository.save(new Employee("Frodo", "Baggins", "thief")));
			
			employeeRepository.findAll().forEach(employee -> log.info("Preloaded: " + employee));
			
			orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
			orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));
			
			orderRepository.findAll().forEach(order -> {
				log.info("Preloaded" + order);
			});
		};
	}
}
