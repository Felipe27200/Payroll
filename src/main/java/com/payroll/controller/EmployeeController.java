package com.payroll.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payroll.repository.EmployeeRepository;
import com.payroll.entity.Employee;
import com.payroll.errors.EmployeeNotFoundException;

/*
 * +---------------+
 * | STATIC HELPER |
 * +---------------+
 * 
 * Se debe importar este recurso para poder
 * construir estos links.
 * */
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/*
 * +---------------+
 * | RESPONSE BODY |
 * +---------------+
 * 
 * Indica que la data retornada por cada método
 * será escrita directo dentro del RESPONSE BODY,
 * en lugar de ser renderizada en un template.
 * 
 * This application works with JSON format.
 * */
@RestController
public class EmployeeController 
{
	/*
	 * +----------------------+
	 * | DEPENDENCY INJECTION |
	 * +----------------------+
	 * 
	 * EmployeeRepository es inyecta por el
	 * constructor dentro del controller.
	 * */
	private final EmployeeRepository repository;
	
	public EmployeeController(EmployeeRepository repository)
	{
		this.repository = repository;
	}
	
	@GetMapping("/employees")
	/*
	 * +-----------------------------------------+
	 * | ENCAPSULLATING COLLECTIONS OF RESOURCES |
	 * +-----------------------------------------+
	 * 
	 * CollectionModel<> es un contenedor de HATEOAS,
	 * su propósito es encapsular colecciones de recursos,
	 * en lugar de un solo tipo de recursos como EntityModel<>,
	 * con este contenedor también se pueden incluir enlaces.
	 * */
	public CollectionModel<EntityModel<Employee>> all()
	{
		List<EntityModel<Employee>> employees = 
			repository.findAll().stream()
			.map(employee -> EntityModel.of(employee,
				linkTo(
					methodOn(EmployeeController.class).one(employee.getId())
				).withSelfRel(),
				linkTo(
					methodOn(EmployeeController.class).all()
				).withRel("employees")
			))
			.collect(Collectors.toList());
		
		return CollectionModel.of(employees,
			linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	
	@PostMapping("/employees")
	public Employee newEmployee(@RequestBody Employee newEmployee)
	{
		return repository.save(newEmployee);
	}
	
	@GetMapping("/employees/{id}")
	/*
	 * El valor de retorno es un EntityModel, que 
	 * es un contenedor génerico de Spring HATEOAS
	 * que incluye no solo los datos, sino también
	 * una colección de links.
	 * */
	public EntityModel<Employee> one(@PathVariable Long id)
	{
		Employee employee = repository.findById(id)
				/* 
				 * +-------------------+
				 * | HANDLE EXCEPTIONS |
				 * +-------------------+
				 * 
				 * Se lanza una excepción cuando se hace la busqueda
				 * del Employee, pero este no es encontrado.
				 * 
				 * Cuando se lanza la excepción se utiliza esta 
				 * información adicional de Spring MVC configuration
				 * para usarla en renderizar un HTTP 404
				 */
			      .orElseThrow(() -> new EmployeeNotFoundException(id));
		
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
			linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
			/*
			 * Pide a Spring HATEOAS que construya un link para el conglomerado
			 * raíz (root), all(), y lo llame "employees", indica la ruta raíz
			 * para el controlador, apuntando al método all() del mismo.
			 * */
			linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}
	
	@PutMapping("/employees/{id}")
	public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id)
	{
		return repository.findById(id)
			.map(employee -> {
				employee.setName(newEmployee.getName());
				employee.setRole(newEmployee.getRole());
				
				return repository.save(employee);
			})
			.orElseGet(() -> {
				newEmployee.setId(id);
				
				return repository.save(newEmployee);
			});
	}
	
	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable Long id)
	{
		repository.deleteById(id);
	}
}
