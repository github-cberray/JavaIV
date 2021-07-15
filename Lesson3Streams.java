package assignment3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Lesson3Stream.java - This application assesses a file, retrieves and counts text string expressions. It compares 
 * doing so in both a parallel stream and a non-parallel stream, comparing the time required for each.  This
 * information is displayed to the console.
 * 
 * @author Charles S Berray, cberray@gmail.com
 * @version 1.0 7.13.2021
 * 
 * for class: 157160 Java Programming IV Advanced Java Programming
 * for Professor: Norman McEntire, norman.mcentire@gmail.com
 */
public class Lesson3Streams {

	/*
	 * makeStringBigger - function concatenates multiple copies of a given string, returning an array list of words
	 * @param 	s			String		the input string
	 * @param 	multiplier	int			the number of the input strings desired to be merged together
	 * returns List<String>	 from the white space separated string multiples
	 * 
	 */
	public static List<String> makeStringBigger(String s, int multiplier) {
		int n=1;
		while (n<multiplier) {
			s=s.concat(s);
			n++;
		}
		return Arrays.asList(s.split("\\s*,\\s*"));//split by any white or comma
	}

	/*
	 * streamRun - function to count occurrences of a regex string in a parent string using a stream
	 * @param 	strDump		String		the input string
	 * @param 	multiplier	int			the number of the input strings desired to be merged together
	 * @param	hexRegEx		String		the regular expression string used to search for a specific group of chars
	 * 
	 * returns time in milliseconds to count the filtered regex from the multiplied string expression
	 */
	public static long streamRun(String strDump, int multiplier, String hexRegEx) {
		long startTime = System.currentTimeMillis();
		List<String> wordList=makeStringBigger(strDump,multiplier);
		Pattern pattern = Pattern.compile(hexRegEx);
		long longCount = wordList.stream()
				//.filter(w->w.matches(hexRegEx))
				.filter(pattern.asPredicate())
				//.filter(w->w.length()>15)
				.count();
		long endTime = System.currentTimeMillis();
		//System.out.println("Stream Count: "+longCount);
		return endTime-startTime;
	}

	/*
	 * parallelStreamRun - function to count occurrences of a regex string in a parent string using a parallel stream
	 * @param 	strDump		String		the input string
	 * @param 	multiplier	int			the number of the input strings desired to be merged together
	 * @param	hexRegEx		String		the regular expression string used to search for a specific group of chars
	 * 
	 * returns time in milliseconds to count the filtered regex from the multiplied string expression
	 */
	public static long parallelStreamRun(String strDump, int multiplier, String hexRegEx) {
		long startTime = System.currentTimeMillis();
		List<String> wordList=makeStringBigger(strDump,multiplier);

		Pattern pattern = Pattern.compile(hexRegEx);
		long longCount = wordList.parallelStream()
				//.filter(w->w.matches(hexRegEx))
				.filter(pattern.asPredicate())
				//.filter(w->w.length()>9)
				//.filter(w->w.)
				.count();
		long endTime = System.currentTimeMillis();
		//System.out.println("Parallel Count: "+longCount);
		return endTime-startTime;
	}

	/*
	 * parallelStreamCount - function to whitespace separated strings in a parent string
	 * @param 	strDump		String		the input string
	 * @param 	multiplier	int			the number of the input strings desired to be merged together
	 * 
	 * returns long count of number of whitespace separated strings
	 */
	public static long parallelStreamCount(String strDump, int multiplier) {

		List<String> wordList=makeStringBigger(strDump,multiplier);
		long longCount = wordList.parallelStream()
				.count();
		return longCount;
	}

	/*
	 * printResults - examines multiple long values and builds a custom string from the results
	 *   to indicate whether the parallel or stream is faster and by how many mils
	 *   
	 *   @param		milStream		long		the millisecs to run the stream count process
	 *   @param		milParStream	long		the millisecs to run the parallel stream count process
	 *   
	 *   returns string showing the timing and which string was faster
	 */
	public static String printResults(long milStream, long milParStream) {
		long parallelFaster=milStream- milParStream;
		String fasterOne;
		String slowerOne;
		if (parallelFaster>0 ) {
			fasterOne = "ParallelStream";
			slowerOne = "Stream";
		}else {
			slowerOne = "ParallelStream";
			fasterOne = "Stream";
			parallelFaster=-parallelFaster;
		}
		return ("Results: "+fasterOne+" was "+ parallelFaster + " ms faster than "+slowerOne);
	}

	/*
	 * main - retrieves input file, resolves the content first to a bytestream, then a string. Next, it uses
	 * pattern and matcher to identify occurrences of regular expression. Next, it times out the time for a 
	 * stream and then parallel stream process to conduct the filter and count process.  Finally, it repeats this process 
	 * but for merged concatenations of the original string, to determine when a parallel stream becomes faster.
	 */
	public static void main(String[] args) throws IOException {
		String fasterOne;
		String slowerOne;
		long parallelFaster;

		// Read in the file as a string
		String filename = "JobResult_124432.txt";
		FileInputStream fis = new FileInputStream(filename);
		byte [] byteStream = fis.readAllBytes();
		String strDump = new String(byteStream,Charset.forName("UTF-8"));
		int byteSize=byteStream.length;
		fis.close();

		// Produce the output header information to the console
		System.out.print("1. Processed the following input file into a string: ");
		System.out.println(filename);
		System.out.println();
		System.out.println("2. String size is: "+byteSize);

		// Get the number of times the pattern 00000000nnnnnnnn repeats
		String hexRegEx = "00000000+[0-9A-Fa-f]{8}";
		//String hexRegEx = "[0-9A-Za-z]{15}";
		Pattern hexPatternCounter=Pattern.compile(hexRegEx);
		Matcher hexCounter = hexPatternCounter.matcher(strDump);
		int x=0;
		while (hexCounter.find()) { 
			x++;
		} 

		// Produce the findings to the console
		System.out.println("   Timing of finding hex value occurrences(Total of "+x+")");
		System.out.print("   - Try 1:");
		System.out.println(" String Size: "+byteSize);

		//STREAM - non parallel stream used to filter and count a regular expression in source string
		long milStream=streamRun(strDump,1, hexRegEx);
		System.out.println("   - Millisecs using stream: "+ milStream + " milliseconds");

		//STREAM - PARALLEL stream used to filter and count a regular expression in source string
		long milParStream=parallelStreamRun(strDump,1, hexRegEx);
		System.out.println("   - Millisecs using parallel stream: "+ milParStream+ " milliseconds");

		//RESULTS - indicates the faster of the two methods, stream or parallel
		System.out.println(printResults(milStream,milParStream));

		//Now, let's do that again with double, triple, quadruple...
		int iteration = 2;
		while (milStream<=milParStream) {
			System.out.println();
			System.out.println("Try "+iteration+": String Size: "+(strDump.length()*iteration)+" ("+iteration+"x)");
			//System.out.println(parallelStreamCount(strDump,iteration)+" strings");
			milStream=streamRun(strDump,iteration,hexRegEx);
			milParStream=parallelStreamRun(strDump,iteration,hexRegEx);
			System.out.println(printResults(milStream,milParStream));
			iteration++;
			if (iteration>20) {milStream=1000000000;}//limit the iterations to 20
		}
		System.out.println("All done!");
	}

}
