package com.payroll.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
import com.payroll.model.EmployeeModelAssembler;

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
	 * EmployeeRepository es inyectada por el
	 * constructor dentro del controller.
	 * */
	private final EmployeeRepository repository;
	private final EmployeeModelAssembler assembler;
	
	/*
	 * Para sacar ventaja del ensamblador hay que 
	 * inyectarlo en el constructor del controlador.
	 * */
	public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler)
	{
		this.repository = repository;
		this.assembler = assembler;
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
		List<EntityModel<Employee>> employees = repository.findAll().stream()
				/*
				 * El assembler se encara de toda la creación del modelo
				 * EntityModel<Employee>.
				 * */
			.map(assembler::toModel)
			.collect(Collectors.toList());
		
		return CollectionModel.of(employees,
			linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	
	@PostMapping("/employees")
	/* +-------------------------+
	 * | EMMIT A 201 HTTP STATUS |
	 * +-------------------------+
	 * 
	 * ResponseEntity es usado para crear una 
	 * HTTP 201 status message.
	 * 
	 * Este tipo de respuesta generalmente incluye un Location response header,
	 * y se usará la URL derivada del self-related link del modelo.
	 */
	public ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee)
	{
		/*
		 * +--------------------------------------+
		 * | SAVING DATA AND WRAPPED TO ASSEMBLER |
		 * +--------------------------------------+
		 * 
		 * Funciona de forma similar el almacenado, sólo que
		 * el objeto resultante es envuelto usando EmployeeModelAssembler.
		 * */
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
		
		/*
		 * Además, retorna la versión basada en los modelos del objeto guardado.
		 * */
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
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
		 * +-------------------+
		 * | USE THE ASSEMBLER |
		 * +-------------------+
		 * 
		 * Se usa el método del ensamblador que hace 
		 * lo mismo que el código que yacía en esta sección. 
		 * */
		return assembler.toModel(employee);

	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id)
	{
		Employee updatedEmployee = repository.findById(id)
			.map(employee -> {
				employee.setName(newEmployee.getName());
				employee.setRole(newEmployee.getRole());
				
				return repository.save(employee);
			})
			.orElseGet(() -> {
				newEmployee.setId(id);
				
				return repository.save(newEmployee);
			});
		
		/*
		 * El objeto construido desde el save() es luego envuelto en el 
		 * EmployeeModelAssembler usando un objeto EntityModel<Employee>.
		 * */
		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
		
		/*
		 * Para obtener una respuesta HTTP más detallada que un 200 OK, 
		 * se usa ResponseEntity de Spring MVC.
		 * */
		return ResponseEntity
			/*
			 * Usando el método getRequiredLink se puede recuperar el Link creado
			 * por EmployeeModelAssembler con un SELF rel.
			 * 
			 * Este método retorna un Link que debe ser convertido en URI con 
			 * el método toUri();
			 * 
			 * En el método created() podemos introducir la URI del recuroso.
			 * */
			.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			.body(entityModel);
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id)
	{
		repository.deleteById(id);
		
		/*
		 * Retorna una respuesta HTTP 204 No Content
		 * */
		return ResponseEntity.noContent().build();
	}
}
