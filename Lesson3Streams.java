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

public class Lesson3Streams {

	public static List<String> makeStringBigger(String s, int multiplier) {
		int n=1;
		while (n<multiplier) {
			//s=s.concat(s);
			n++;
		}
		return Arrays.asList(s.split("\\PL+"));
	}
	
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
		System.out.println("Stream Count: "+longCount);
		return endTime-startTime;
	}
	
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
		System.out.println("Parallel Count: "+longCount);
		//endTime=endTime+4;
		return endTime-startTime;
	}
	public static long parallelStreamCount(String strDump, int multiplier) {

		List<String> wordList=makeStringBigger(strDump,multiplier);
		long longCount = wordList.parallelStream()
				.count();
		return longCount;
	}
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
		System.out.println("2a. String size is: "+byteSize);

		// Get the number of times the pattern 00000000nnnnnnnn repeats
		String hexRegEx = "[+-]?0000000[0-9]+|0[Xx][0-9A-Fa-f]+";
		//String hexRegEx = "[0-9A-Za-z]{15}";
		Pattern hexPatternCounter=Pattern.compile(hexRegEx);
		Matcher hexCounter = hexPatternCounter.matcher(strDump);
		int x=0;
		while (hexCounter.find()) { 
			x++;
		} 
		
		// Produce the findings to the console
		System.out.println("2b. Timing of finding hex value occurrences(Total of "+x+")");
		System.out.print("  Try 1:");
		System.out.println(" String Size: "+byteSize);

		//STREAM - non parallel
		long milStream=streamRun(strDump,1, hexRegEx);
		System.out.println("  Millisecs using stream: "+ milStream + " milliseconds");

		//STREAM - PARALLEL
		long milParStream=parallelStreamRun(strDump,1, hexRegEx);
		System.out.println("  Millisecs using parallel stream: "+ milParStream+ " milliseconds");

		//RESULTS	
		System.out.println(printResults(milStream,milParStream));
		
		//Now, let's do that again with double, triple, quadruple...
		int iteration = 2;
		while (milStream<milParStream) {
			System.out.println("Try "+iteration+": String Size: "+(strDump.length()*iteration));
			System.out.println(parallelStreamCount(strDump,iteration)+" strings");
			milStream=streamRun(strDump,iteration,hexRegEx);
			milParStream=parallelStreamRun(strDump,iteration,hexRegEx);
			System.out.println(printResults(milStream,milParStream));
			iteration++;
			if (iteration>20) {milStream=1000000000;}
		}
	}

}
