package assignment2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Lesson2RegEx
 * 
 * A Java console application that reads in the neighbor-dump.txt file
 * and uses Regular Expressions to generate output to the console.
 * 
 * @author Charles S Berray, cberray@gmail.com
 * @version 1.0 7.11.2021
 * 
 * for class: 157160 Java Programming IV Advanced Java Programming
 * for Professor: Norman McEntire, norman.mcentire@gmail.com
 */
public class Lesson2RegEx {

	public static void main(String[] args) throws IOException {

		// Read in the file as a string
		String filename = "neighbor-dump.txt";
		FileInputStream fis = new FileInputStream(filename);
		byte [] byteStream = fis.readAllBytes();
		String strDump = new String(byteStream,Charset.forName("UTF-8"));

		// Produce the output header information to the console
		System.out.println("$ java Lesson2RegEx " + filename);
		System.out.println("Processed the following input file: ");
		System.out.println(filename);
		System.out.println();
		System.out.println("Results are as follows: ");
		System.out.println();
		
		
		// PAN ID - Build regex and pattern for the PAN ID
		String panRegEx = "PANID.*";
		Pattern panPattern = Pattern.compile(panRegEx);
		Matcher panMatcher = panPattern.matcher(strDump);
		
		// Count the number of occurrences found
		Pattern panPatternCounter=Pattern.compile(panRegEx);
		Matcher panCounter = panPatternCounter.matcher(strDump);
		int x=0;
		while (panCounter.find()) { 
			x++;
		} 
		
		// Produce the findings to the console
		System.out.println("- List of PAN IDs (Total of "+x+")");
		while (panMatcher.find()) { 
			String match = panMatcher.group(); 
			System.out.println(match); 
		} 
		System.out.println();


		// MAC ADDRESS - Build regex and pattern for the MAC String macRegEx
		String macRegEx = "000[0-9a-fA-F]{13}";
		Pattern macPattern = Pattern.compile(macRegEx);
		Matcher macMatcher = macPattern.matcher(strDump);
		
		// Count the number of occurrences found
		Pattern macPatternCounter = Pattern.compile(macRegEx);
		Matcher macMatcherCounter = macPatternCounter.matcher(strDump);
		x=0;
		while (macMatcherCounter.find()) { 
			x++;
		} 
		
		// Produce the findings to the console
		System.out.println("- List of MAC Addresses (Total of "+x+")");
		// build an array of these mac addresses so that we can use them later with the RSSI display
		String[] macString = new String[100];
		int num=0;
		while (macMatcher.find()) { 
			String match = macMatcher.group(); 
			macString[num]=match;
			System.out.println(macString[num]); 
			num++;
		} 
		System.out.println();

		
		// RF RSSI M - Build regex and pattern for the RSSI String rssiRegEx
		// First find string following mac address
		String rssiRegEx = "000[0-9a-fA-F]{13}.*";
		Pattern rssiPattern = Pattern.compile(rssiRegEx);
		Matcher rssiMatcher = rssiPattern.matcher(strDump);
		System.out.println("- List of RF_RSSI_M values for each MAC address ");
		int mm=0;
		while (rssiMatcher.find()) { 
			String match = rssiMatcher.group(); 
			// separate values by spaces
			String [] aRow = match.split("[\s]+");
			// Ignore all but the RSSI M which is in the seventh value of the array
			System.out.println(macString[mm] +" " +aRow[6]);
			mm++;
		} 
		
		// release the resources
		fis.close();

	}

}
