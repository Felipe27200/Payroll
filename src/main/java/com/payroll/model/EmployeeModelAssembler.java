package com.payroll.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.payroll.controller.EmployeeController;
import com.payroll.entity.Employee;

/*
 * +------------------------+
 * | ASSEMBLER ADD THE CODE |
 * +------------------------+
 * 
 * Gracias a la anotación @Component
 * el assembler(ensamblador), creará este
 * code automáticamente cuando la app inicie.
 * */
@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>>{
	@Override 
	/*
	 * El método toModel(), está basado en convertir
	 * a non-model object (Employee) en un model-based
	 * object(EntityModel<Employee>)
	 * */
	public EntityModel<Employee> toModel(Employee employee)
	{
		/*
		 * +-----------------+
		 * | HAL - MEDIATYPE |
		 * +-----------------+
		 * 
		 * Retorna todo en un mediatype conocido como HAL
		 * que no solo códifica los datos sino que también lo
		 * hace con los controles hypermedia, alertando a los 
		 * consumidores sobre las otras partes de la API a las
		 * que pueden navegar.
		 * */
		return EntityModel.of(employee,
			/*
			 * Pide al Spring HATEOAS que construya un link para el método
			 * one() de EmployeeController y lo marque como un enlace propio.
			 * */
			linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
			/*
			 * Pide a Spring HATEOAS que construya un link para el conglomerado
			 * raíz (root), all(), y lo llame "employees", indica la ruta raíz
			 * para el controlador, apuntando al método all() del mismo.
			 * */
			linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}
}
