package assignments;

import java.time.*;
import java.util.*;
import java.io.*;

/*
 * Employee.java
 * 
 * Object contains values for the name (string), salary(double) and hire date (int year, month, day)
 * it also has methods to read and write an employee
 * 
 * In addition, there are getters and setters.
 * 
 * @author Charles Berray
 * @version: code shared from CORE JAVA, VOLUME II -- ADVANCED FEATURES: 2 (CORE SERIES) by Horstman, Cay S., p.152
 */
public class Employee implements Serializable {
	private String name;
	private double salary;
	private LocalDate hireDate;

	/*
	 * Employee - an object constructed from the parameters of
	 * @param n			string	- the name of the employee
	 * @param s			double	- the annual salary of the employee
	 * @param year		int		- the year the employee was hired
	 * @param month		int		- the month the employee was hired
	 * @param day		int		- the day the employee was hired
	 */
	public Employee(String n, double s, int year, int month, int day) {
		name = n;
		salary = s;
		hireDate = LocalDate.of(year, month, day);
	}

/*
 * Employee - an object constructed from a line of text
 * The line is formatted with name | salary | datehire in year-mo-day
 */
	public Employee(String line) {
		
		String[] tokens = line.split("\\|");
		String name = tokens[0];
		double salary = Double.parseDouble(tokens[1]);
		
		String dateString = tokens[2];
		String[] dateBreak = dateString.split("\\-");
		
		int year = Integer.parseInt(dateBreak[0].trim());
		int month = Integer.parseInt(dateBreak[1].trim());
		int day = Integer.parseInt(dateBreak[2].trim());
		
		LocalDate hireDate = LocalDate.of(year, month, day);
		
		setName(name);
		setSalary(salary);
		setHireDate(hireDate);
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	public String getName() { return name; }
	public double getSalary() { return salary; }
	public String getHireDayString() { return hireDate.toString(); }
	public void setHireDate(LocalDate hireDate) {		this.hireDate = hireDate;	}
	public void setName(String name) {		this.name = name;	}
	public void setSalary(double salary) {		this.salary = salary;	}
	

	/*
	 * readEmployee - Retrieves an employee object from a line of text separated by | from scanner
	 * @version: code shared from CORE JAVA, VOLUME II -- ADVANCED FEATURES: 2 (CORE SERIES) by Horstman, Cay S., p.152
	 */
	public static Employee readEmployee(Scanner in) {
		String line = in.nextLine();
		String[] tokens = line.split("\\|");
		String name = tokens[0];
		double salary = Double.parseDouble(tokens[1]);
		String dateString = tokens[2];
		String[] dateBreak = dateString.split("\\-");
		
		int year = Integer.parseInt(dateBreak[0].trim());
		int month = Integer.parseInt(dateBreak[1].trim());
		int day = Integer.parseInt(dateBreak[2].trim());

		return new Employee(name, salary, year, month, day);
	}
	
	/*
	 * writeEmployee - Takes an employee object and writes it into text with | separators
	 * @version: code shared from CORE JAVA, VOLUME II -- ADVANCED FEATURES: 2 (CORE SERIES) by Horstman, Cay S., p.152
	 */
	public static void writeEmployee(PrintWriter out, Employee e) {
		out.println(e.getName()+"|"+e.getSalary()+"|"+e.getHireDayString());
	}
	
	/*
	 * writeEmployee from employee object to console
	 */
	public static void writeEmployee(Employee e) {
		System.out.println(e.getName()+"|"+e.getSalary()+"|"+e.getHireDayString());
	}
}