import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class provides helper functions to decode various types of files.
 * 
 * It includes methods which read from files and parse them into varying desired formats, such as
 * 1) byte arrays
 * 2) hexadecimal strings
 * 3) binary strings
 * 4) base64 strings
 * 
 * others may be implemented later
 * @author drew
 *
 */
public class FileDecoder {

	/**
	 * Given a filename as a string, parses file in base 64 and gives back as a byte array 
	 * @param filename
	 * @return a byte array containing all of the file
	 */
	public static byte[] parseBase64FileToByteArray(String filename) {
		String asStr = strListToOneString(fileToStrList(filename));
		byte toRet[] = ByteWiseOps.base64StringToByteArray(asStr);
		
		return toRet;
		
	}
	
	/**
	 * Given a file name which refers to a file comprising of encoded hexadecimal, return its contents
	 * as a byte array
	 * @param filename
	 * @return a byte array
	 */
	public static byte[] parseHexFileToByteArray(String filename) {
		String asStr = strListToOneString(fileToStrList(filename));
		
		return ByteWiseOps.hexStringtoByteArray(asStr);
	}
	
	/**
	 * Given a file, returns a newline-delimited arraylist of its contents
	 * @param filename
	 * @return
	 */
	public static ArrayList<String> fileToStrList(String filename) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		ArrayList<String> all = new ArrayList<String>();
		String current = "";
		//System.out.println("Raw: ");
		try {
			while (reader.ready()) {
				current = reader.readLine();
				//System.out.println(current);
				all.add(current);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return all;
	}
	
	/**
	 * Given an arraylist of strings, return a single string comprised of each entry in the list
	 * @param l the list
	 * @return the string
	 */
	private static String strListToOneString(ArrayList<String> l) {
		String toRet = "";
		for (int i = 0; i < l.size(); i++) {
			toRet+=l.get(i);
		}
		return toRet;
	}
	
}
