package com.payroll.errors;

import org.springframework.http.HttpStatus;
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
public class OrderNotFoundAdvice {
	
	/*
	 * Indica que este advice se renderiza 
	 * dentro del cuerpo de la respuesta.
	 * */
	@ResponseBody
	
	/*
	 * Configura el advice para responder solo
	 * si OrderNotFoundException es lanzada.
	 * */
	@ExceptionHandler(OrderNotFoundException.class)
	
	/*
	 * Indica que se emita un HttpStatus.NOT_FOUND,
	 * es decir, un HTTP 404.
	 * */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String orderNotFoundHandler(OrderNotFoundException ex)
	{
		return ex.getMessage();
	}
}
