package com.payroll.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * +---------------------+
 * | RENDER THE HTTP 404 |
 * +---------------------+
 * 
 * El cuerpo del advice genera el contenido,
 * en este caso proporciona el mensaje de la 
 * EXCEPTION.
 * */
@ControllerAdvice
public class EmployeeNotFoundAdvice {
	
	/*
	 * Indica que este advice se renderiza 
	 * dentro del cuerpo de la respuesta.
	 * */
	@ResponseBody
	
	/*
	 * Configura el advice para responder solo
	 * si EmployeeNotFoundException es lanzada.
	 * */
	@ExceptionHandler(EmployeeNotFoundException.class)
	
	/*
	 * Indica que se emita un HttpStatus.NOT_FOUND,
	 * es decir, un HTTP 404.
	 * */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String employeeNotFoundHandler(EmployeeNotFoundException ex)
	{
		return ex.getMessage();
	}
}
