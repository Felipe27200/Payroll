package com.payroll.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Employee {
	/*
	 * Id con Autoincrement por default
	 */
	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	private String role;

	/*
	 * The Default constructor is necessary
	 */
	public Employee() { }

	public Employee(String firstName, String lastName, String role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
	
	/*
	 * +--------------------------------+
	 * | SIMULAR LA ESTRUCTURA ANTERIOR |
	 * +--------------------------------+
	 * 
	 * Getter and setter to imitate the 
	 * functionality of old structure.
	 * 
	 * setName() -> gracias a él la API puede
	 * recibir un atributo name en una petición
	 * y desfragmentarlo para que se acople al
	 * anterior funcionamiento, cuando solo tenía una
	 * propieda name.
	 * 
	 * */
	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
	}

	public Long getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getRole() {
		return this.role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;
		Employee employee = (Employee) o;
		return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName)
				&& Objects.equals(this.lastName, employee.lastName) && Objects.equals(this.role, employee.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.firstName, this.lastName, this.role);
	}

	@Override
	public String toString() {
		return "Employee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
				+ '\'' + ", role='" + this.role + '\'' + '}';
	}
}