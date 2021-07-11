package assignments;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class Lesson1IOStream {
/**
 * Lesson1IOStream.java
 * 
 * a Java console application named that (1) writes and then (2) reads an Employee object, 
 * with help info displayed, text write and read as text file, binary write and read, 
 * and object write and read onto the console.
 * 
 * @author Charles S Berray, cberray@gmail.com
 * @version 1.0 7.11.2021
 * 
 * for class: 157160 Java Programming IV Advanced Java Programming
 * for Professor: Norman McEntire, norman.mcentire@gmail.com
 * 
 * @param args
 * @throws IOException 
 * @throws FileNotFoundException 
 * @throws ClassNotFoundException 
 */
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		String working_dir = System.getProperty("user.dir");
		/*
		 * HELP
		 * This section displays help info on the console
		 */
		System.out.println(" ________________________________________________________________________________");
		System.out.println("|  Help Information                                                              |");
		System.out.println("|                                                                                |");
		System.out.println("|  This routine will load two employees into a text file, and then demonstrate   |");
		System.out.println("|   the concepts of working with I/O.                                            |");
		System.out.println("|                                                                                |");
		System.out.println("|  The output files will be located at: "+working_dir +"  |");
		System.out.println("|________________________________________________________________________________|");
		System.out.println();

		/*
		 * TEXT
		 *  This section writes/reads as text file and displays results on console
		 */
		// First, let's read in the data
		String john = "John Doe | 123 | 1980-9-10";
		String sally = "Sally Smith | 456 | 1990 -11-12";
		// Construct employee objects
		Employee johnEmployee = new Employee(john);
		Employee sallyEmployee = new Employee(sally);
		// Construct printwriter and send employees to file employee.txt
		PrintWriter pwOut = new PrintWriter("employee.txt");
		Employee.writeEmployee(pwOut, johnEmployee);
		Employee.writeEmployee(pwOut, sallyEmployee);
		pwOut.close();

		//Read Text File into String
		String filename = "Employee.txt";
		//String pathname = System.getProperty("user.dir")+ File.separator + filename;
		//Path path = Paths.get(pathname);
		InputStream fis = new FileInputStream(filename);
		byte [] textInput = fis.readAllBytes();
		System.out.println("Here is the output from text write/read: ");
		System.out.println(new String(textInput, StandardCharsets.UTF_8));
		fis.close();
		
		
		/*
		 * BINARY
		 * This section uses binary to write/read as binary file and displays results on console
		 */
		//  Write to output file
		String outputFile = "binary.dat";		
		try (
				//  read in information from the text file
				InputStream inputStream = new FileInputStream(filename);
				//  prepare to write in binary to outputfile
				FileOutputStream fileOS = new FileOutputStream(outputFile);
				ObjectOutputStream os = new ObjectOutputStream(fileOS);
				) {
			//  write to the file using binary
			int byteRead;
			while ((byteRead = inputStream.read()) != -1) {
				os.write(byteRead);
			}
			os.close();
		} catch (IOException ex) {			ex.printStackTrace();		}

		//  Read the binary from the output file
		try {
			FileInputStream fInput = new FileInputStream(outputFile);
			ObjectInputStream ois = new ObjectInputStream(fInput);
			byte[] theData = ois.readAllBytes();
			System.out.println("Here is the output from binary write/read: ");
			System.out.println(new String(theData, StandardCharsets.UTF_8));
			ois.close();
		} catch (FileNotFoundException e) {			e.printStackTrace();
		} catch (IOException e) {			e.printStackTrace();		}

		
		/*
		 * LINES
		 * Read text into lines List<String> lines = Files.readAllLines(path)
		 */
		System.out.println("Here is the output from lines write/read: ");
		//  Read Text File into String
		filename = "employee.txt";
		String pathname = System.getProperty("user.dir")+ File.separator + filename;
		Path path = Paths.get(pathname);
		//  Read text into lines
		List<String> lines = Files.readAllLines(path);
		//  Write text from lines to console
		for (String sl:lines ) {
			System.out.println(sl);
		}
		System.out.println();
		
		
		/*
		 * OBJECT
		 * This section works with an object to writes/reads as object file and displays results on console
		 * This section is largely taken from class examples
		 */

		// employees are already saved as objects from the text section of the assignment

		// Create an employee array 
		Employee[] employees = new Employee[2]; 
		employees[0] = johnEmployee; 
		employees[1] = sallyEmployee;

		//Save the objects 
		filename = "employeeObjects.txt";

		try ( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename)))
		{ 
			oos.writeObject(employees); 
		}

		// Read all records 
		try ( ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) 
		{

			// Read in the array from the file
			Employee[] newEmployees = (Employee[]) ois.readObject();

			// Display all records 		
			System.out.println("Here is the output from objects write/read: ");

			for (Employee e : newEmployees) {
				Employee.writeEmployee(e);
			}
			ois.close();

		}

	}

}
