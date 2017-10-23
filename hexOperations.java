
/**
 * Performs various operations on hex bytes, as specified
 * in the Cryptopals challenge, or written as needed
 * @author drew
 *
 */


import java.util.Base64;
import java.util.Stack;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64.*;
import java.util.Collections;
public class hexOperations {
	
	private static final char MY_CHARS[] = {'e','t','a','i','n',' ','s','h','r','d','l','u','E','T','A','I','N','S','H','R','D','L','U'};
	private static final int MAX_KEY_SIZE = 40;
	private static final int MIN_KEY_SIZE = 1;
	private static final int MAX_BYTE = 255;


	/**
	 * Given a String, return its letter score. Letter score is the number of occurrences of
	 * the characters in "ETAOIN SHRDLU" (ignore case)
	 * @param s
	 * @return
	 */
	public static int getLetterScore(byte[] bytes) {
		
		
		int score = 0;
		/*
		for (int i = 0; i < bytes.length; i++) {
			int current = (int)bytes[i];
			if (current > 96 && current < 123) {
				score+=2;
			}
			if (current > 32 && current < 127)
				score+=1;
			else if (current==32)
				score+=3;
			else
				score-=5;
		}
		*/
		score+= getLowerFrequencyScore(bytes);
		score += getCapFrequencyScore(bytes);
		score+= getBadCharFrequencyScore(bytes);
		return score;
	}
	
	public static int getLowerFrequencyScore(byte[] bytes) {
		int score = 0;
		for (int i = 0; i < bytes.length; i++) {
			int current = (int)bytes[i];
			if (current > 96 && current < 123)
				score+=5;
		}
		return score;
	}
	
	public static int getBadCharFrequencyScore(byte[] bytes) {
		int score = 0;
		for (int i = 0; i < bytes.length; i++) {
			if ((int)bytes[i] < 32 || (int)bytes[i] > 127)
				score-=5;
		}
		
		return score;
	}
	
	public static int getCapFrequencyScore(byte[] bytes) {
		
		int score = 0;
		int count = 0;
		for (int i = 0; i < bytes.length;i++) {
			int current = (int)bytes[i];
			if (current > 64 && current < 90)
				count++;
		}
		double frequency = count / bytes.length;
		if (frequency < .25)
			score = 25;
		else if (frequency < .5)
			score = -10;
		else if (frequency < .75)
			score = -25;
		else 
			score = -50;
		
		
		return score;
	}


	/**
	 * For finding keys, this letter scoring algorithm doesn't consider issues for building words, rather it just prefers letters to unknown
	 * symbols
	 * @param bytes the byte array of bytes to score
	 * @return the score
	 */
	public static int getKeyLetterScore(byte[] bytes) {
		
		int score = 0;
		for (int i = 0; i < bytes.length; i++) {
			int current = (int)bytes[i];
			if (current > 32 && current < 127)
				score+=3;
			else if (current==32)
				score+=5;
			else
				score-=5;
		}
		score+=getBadCharFrequencyScore(bytes);
		return score;
	}
	
	public static String getBestScore(ArrayList<byte[]> allBytes) {
		int score_max = 0;
		String bestScore = "";
		//int bestScoreLine = 0;
		int key = 0;
		for (int j = 0; j < allBytes.size(); j++) {
			int scores[] = new int[255];
			String xorResults[] = new String[255];
			byte current[] = allBytes.get(j);
			for (int i = 0; i < xorResults.length; i++) {
				byte b = (byte)i;
				int len = current.length;
				byte testB[] = arrayOfByteValAndLen(b, len);
				byte xorTest[] = xorTwoByteArrays(current, testB);
				//System.out.println("Try: " + i);
				//System.out.println(dSAsAscii);
				String xorAsAscii = "";
				try {
					xorAsAscii = new String(xorTest, "US-ASCII");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(xorAsAscii);
				xorResults[i] = xorAsAscii;
				scores[i] = hexOperations.getLetterScore(xorTest);
				if (scores[i] > score_max) {
					score_max = scores[i];
				//	System.out.println("New best score: " + score_max);
				//	System.out.println("New result: " + xorResults[i]);
					//bestScoreLine = j;
					key = i;
				//	System.out.println("Line number: " + j);
				//	System.out.println("Key: " + i);
					//System.out.println("Before decoding: " + allBytes.get(j));
					bestScore = xorResults[i];
				}
			}
		}		
		//System.out.println("Best score: " + score_max);
		//System.out.println("Decoded String: " + bestScore);
		return bestScore;
		
	}
	
	public static int getBestKey(byte[] bytes) {
		int score_max = 0; //this will be the max score for all keys tried
		//int scores[] = new int[MAX_BYTE]; //all of the scores
		int len = bytes.length; //how many bytes long to make the single key XOR cipher for this block
		//System.out.println("Key length: " + len);
		int key = 0; //the actual key
		//String xorResults[] = new String[MAX_BYTE+1];
		for (int i = 0; i < 128; i++) {
			byte b = (byte)(i);
			byte testB[] = arrayOfByteValAndLen(b, len);
			byte xorTest[] = xorTwoByteArrays(bytes, testB);
			int currentScore = 0;
			String currentAscii = "";
			try {
				currentAscii = new String(xorTest, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			currentScore = hexOperations.getKeyLetterScore(xorTest);
			if (currentScore > score_max) {
				//System.out.println("New best score for this string: " + currentAscii + " with score " + currentScore);
				score_max = currentScore;
				key = i;
			}
		}
	
		return key;
		
	}
	/**
	 * Given a byte array of hexadecimal, returns that array,
	 * but represented as a string and in Base 64.
	 * @param in, a string representing a hexadecimal word. Word length mod 4 must be 0
	 * @return a base 64 byte array
	 */
	public static byte[] hexArrayTo64Array(byte[] hexIn)
	{
		return Base64.getEncoder().encode(hexIn);
		
		
	}
	
	public static String hexArrayTo64String(byte[] hexIn) {
		return Base64.getEncoder().encodeToString(hexIn);
	}
	
	/**
	 * Given a hex string, gives back a byte array
	 * @param ogHex
	 * @return byte array of length ogHex.len/2
	 */
	public static byte[] hexStringtoByteArray(String ogHex) {
		//strip off the 0x, if one is there
		if (ogHex.contains("0x")) {
			ogHex = ogHex.substring(2, ogHex.length());
		}
		int wordL = ogHex.length();
		int byteIndex = 0; //index within byte array
		byte bytes[] = new byte[wordL/2]; //byte array to return, half size because bytes are 2 chars long
		for (int i = 0; i < wordL ; i+=2) {
			//for each index, grab 2 chars from the string, convert them to a byte, add byte to array
			bytes[byteIndex] = (byte) Integer.parseInt(ogHex.substring(i,i + 2), 16); 
			byteIndex++;
		}
		
		return bytes;
	}
	/**
	 * Given a byte array, prints it back as a single, long string, in base the of choice
	 * @param ogBytes hex byte array
	 * @param base, the base
	 */
	public static void printByteArray(byte[] bytes, int base) {
		
		String toPrint = "";
		for (int i = 0; i < bytes.length; i++) {
			toPrint += Integer.toString(bytes[i], base);
		}
		System.out.println(toPrint);
	}
	/**
	 * Given a byte array and a base, returns a string representation of the whole array
	 * @param bytes the array
	 * @param base the base of desired representation (note, can't be higher than 16 ._. )
	 */
	public static String byteArrayToString(byte[] bytes, int base) {
		
		String toRet = "";
		String toAdd = "";
		for (int i = 0; i < bytes.length; i++) {
			toAdd = Integer.toString(bytes[i], 16);
			if (toAdd.length() < 2) {
				toAdd = "0" + toAdd;
			}
			toRet += toAdd;
		}
		return toRet;
	}
	
	/**
	 * Given two byte arrays, returns a new byte array consisting of the results of
	 * XOR'ing each byte from the given arrays
	 * @param b1 byte array 1
	 * @param b2 byte array 2
	 * @return XORArray, both arrays XOR'd
	 */
	public static byte[] xorTwoByteArrays(byte[] b1, byte[] b2) {
		byte[] b3 = new byte[b1.length];
		for (int i = 0; i < b3.length; i++) {
			int b1C = (int)b1[i];
			int b2C = (int)b2[i];
			int xor = b1C ^ b2C;
			//System.out.println("Byte 1: " + Integer.toBinaryString(b1C) 
			//+ "\nByte 2: " + Integer.toBinaryString(b2C) + "\nXOR: " + Integer.toBinaryString(xor));
			b3[i] = (byte)xor;
		}
		return b3;
	}
	/**
	 * XOR's two bytes, returns result
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static byte xorTwoBytes(byte b1, byte b2)
	{
		int b1C = (int)b1;
		int b2C = (int)b2;
		int xor = b1C ^ b2C;
		
		return (byte)xor;
	}
	/**
	 * Given a text passage to encrypt and a key with which to encrypt it, returns a new string that is
	 * the text, but encrypted with the key.
	 * @param text
	 * @param key
	 * @return
	 */
	public static String repeatingKeyXORFromStrings(String text, String key) {
		String toRet = "";
		byte keyBytes[] = asciiStrToByteArray(key);
		byte textBytes[] = asciiStrToByteArray(text);
		
		byte encrypted[] = hexOperations.repeatingKeyXORFromByteArrays(textBytes, keyBytes);
		toRet = hexOperations.byteArrayToString(encrypted, 16);

		return toRet;
	}
	
	/**
	 * Given a String in US-ASCII form, return a byte array. This is only a few lines of code, but
	 * you have to try/catch every time so it's horribly clunky to not have a function for.
	 * @param s the string in ASCII
	 * @return a byte array of the string
	 */
	public static byte[] asciiStrToByteArray(String s) {
		byte keyBytes[] = null;
		try {
			keyBytes = s.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return keyBytes;
	}
	/**
	 * Given a key and a message to XOR over (in byte forms), returns a new set of bytes.
	 * 
	 * The new set of bytes is the message, but each byte from the message is XOR'd with a byte from the key.
	 * The bytes from the key are iterated over.
	 * @param bytes
	 * @param key
	 * @return
	 */
	public static byte[] repeatingKeyXORFromByteArrays(byte[] bytes, byte[] key) {
		System.out.println("Key size: " + key.length);
		byte toRet[] = new byte[bytes.length];
		int keyMin = 0;
		int keyMax = key.length-1;
		int current = 0;
		for (int i = 0; i < bytes.length; i++) {
			toRet[i] = hexOperations.xorTwoBytes(bytes[i], key[current]);
			current++;
			if (current > keyMax) {
				current = keyMin;
			}
		}
		
		
		return toRet;
	}
	/**
	 * Given a value for the byte and a length, a number of bytes, returns a byte array
	 * that includes as many repetitions of that value as possible to fit the length
	 * @param val the value of the byte
	 * @param len the number of bytes
	 * @return
	 */
	public static byte[] arrayOfByteValAndLen(byte val, int len) {
		byte bytes[] = new byte[len];
		for (int i = 0; i < len; i++) {
			bytes[i] = val;
		}
		
		return bytes;
	}
	/**
	 * Given an ascii string, return a binary string. 
	 * @param the ascii string
	 * @return the binary string
	**/
	public static String asciiStrToBinString(String s) {
		byte[] s1Bytes = asciiStrToByteArray(s);
		String s1Bin = byteArrayToBinString(s1Bytes);
		return s1Bin;
	}
	/**
	 * Given two binary Strings, computes their hamming distance, that is, the number of differing bits of data
	 * between them
	 * @param s1 the first String
	 * @param s2 the second String
	 * @return an int, that is the number of differing bits
	 */
	public static int hammingDistance(String s1Bin, String s2Bin) {
		//System.out.println("Original lengths: " + s1Bin.length() + " and " + s2Bin.length());
		//add trailing zeros
		//String withZeros[] = addTrailingZeros(s1Bin, s2Bin);
		//s1Bin = withZeros[0];
		//s2Bin = withZeros[1];
		int dist = 0;
		//System.out.println("Before loop lengths: " + s1Bin.length() + " and " + s2Bin.length());
		for (int i = 0; i < s1Bin.length(); i++) {
			char s1 = s1Bin.charAt(i);
			char s2 = s2Bin.charAt(i);
			//System.out.println("Comparing: " + s1 + " and " + s2);
			if (s1 != s2) {
				//System.out.println("Adding to dist");
				dist++;
			}
		}
		//add possible difference of trailing zeros
		return dist;
	}
	/**
	 * Given a byte array, returns a string that is the binary representation of that byte array
	 * @param bytes the byte array
	 * @return a string that is the binary representation
	 */
	public static String byteArrayToBinString(byte[] bytes) {
		String toRet = "";
		for (int i = 0; i < bytes.length;i++) {

			toRet+= byteToBinString(bytes[i]);
		}
		return toRet;
	}
	
	/**
	 * Given a byte, returns a binary string (with enough trailing zeros to make it 8 chars long)
	 * @param b the byte
	 * @return the string, 8 characters long, of string-encoded-binary
	 */
	public static String byteToBinString(byte b) {
		String toRet = "";
		int it = (int)b;
		toRet = Integer.toBinaryString(it);
		while (toRet.length() < 8) {
			toRet = "0" + toRet;
		}
		
		return toRet;
	}

	/**
	 * Given a file name, return an arraylist of strings in which each string is a line from the file.
	 * @param f the fileReader
	 * @return the string comprised of each entry in the arraylist
	 */
	public static ArrayList<String> fileToStringList(String name)
	{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(name));
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
	public static String strListToOneString(ArrayList<String> l) {
		String toRet = "";
		for (int i = 0; i < l.size(); i++) {
			toRet+=l.get(i);
		}
		return toRet;
	}
	/**
	 * Given a String of encoded base 64, return back as a binary string
	 * @param s the base 64 string
	 * @return  the binary string
	 */
	public static String base64StringToBinString(String b64) {
		
		byte b[] = Base64.getDecoder().decode(b64);
		String toRet = byteArrayToBinString(b);
		return toRet;
	}
	/**
	 * Given a string of encoded binary, return as decoded in Ascii
	 * @param bin
	 * @return
	 */
	public static String binStringToAscii(String bin) {
		String toRet = "";
		
		return toRet;
	}
	/**
	 * Given all file data as base 64 string, determine the key size, n, for a repeating-XOR cipher by
	 * minimizing the hamming distance between each n bytes
	 * @param data
	 * @return the key size
	 */
	public static int getKeySize(String data) {
		
		byte bytes[] = Base64.getDecoder().decode(data);
		double best = 100;
		int bestIndex = 0;
		double current = 0;
		for (int i = 1; i < 41; i++) {
			current = testKeySize(bytes, i);
			if (current < best) {
				best = current;
				bestIndex = i;
			}
		}
		System.out.println("Best key size: " + bestIndex + " with hamming distance of " + best);
		return bestIndex;
	}
	/**
	 * Given a set of bytes and a specific key size, return the average of the
	 * hamming distances between each "size" of bytes
	 * @param bytes
	 * @param size
	 * @return
	 */
	public static double testKeySize(byte[] bytes, int size) {
		
		if (size > bytes.length) {
			System.out.println("The key size is bigger than the data set. This is effing impossible. Abort mission.");
			return 0;
		}
		ArrayList<byte[]> blocks = breakUpIntoBlocks(bytes, size);
		
		int total = 0;
		int minDist = 1000;
		int maxDist = 0;
		int calcs = 0;
		for (int i = 0; i+1 < blocks.size(); i++) {
			//for (int j = i+1; j < blocks.size(); j++) {
				String s1 = byteArrayToBinString(blocks.get(0));
				String s2 = byteArrayToBinString(blocks.get(i+1));
				int dist = hammingDistance(s1, s2);
				total += dist;
				if (dist < minDist)
					minDist = dist;
				if (dist > maxDist)
					maxDist = dist;
				calcs++;
			//System.out.println(s1 + "\n" + s2 + " -> " + dist);
			//}
		}
		//int totalBits = bytes.length*8;
		double average = total/bytes.length;
		//double norm = Math.sqrt(totalBits*totalBits - average*(blocks.size()*size));
		//double gauss = gaussDistr(average);
		/*
		System.out.println("For key size : " + size + " : totalDist : " + total + ", total calculations: " + calcs + ", total bits evaluated: " + calcs*totalBits + 
				", average : " + average + ", Norm : " + gauss);
		*/
		return average;
	}
	
	/**
	 * Given a byte array and the key size, find the key assuming it is a repeating XOR cipher. 
	 * @param bytes the bytes
	 * @param keySize the key size
	 * @return the key, as a byte
	 */
	public static String getKeyAndDecode(byte[] bytes, int keySize) {
		
		ArrayList<byte[]> blocks = breakUpIntoBlocks(bytes, keySize);
		//printBlocks(blocks);
		ArrayList<byte[]> trBlocks = transposeBlocks(blocks);
		//printTransposedBlocks(trBlocks);		
		//get the best key for each transposed block
		//this array holds part of the key in each of its indices
		System.out.print("Key : ");
		int keys[] = new int[keySize];
		byte superKey[] = {84, 101, 114, 109, 105, 110, 97, 116, 111, 114, 32, 88, 58, 
				32, 98, 114, 105, 110, 103, 32, 116, 104, 101, 32, 110, 111,
				105, 115, 101};
		for (int i = 0; i < keySize; i++) {
			//keys[i] = superKey[i];
			keys[i] = getBestKey(trBlocks.get(i));
			byte keyi[] = new byte[1];			
			keyi[0] = (byte)keys[i];
			try {
				System.out.print(new String(keyi, "US-ASCII"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		System.out.println(keys[0]);
		
		
		
		
		byte decoded[] = new byte[bytes.length];
		//apply the keys, rebuilding byte array in original form
		for (int i = 0; i < keySize; i++) {
			byte keyArray[] = arrayOfByteValAndLen((byte)superKey[i],trBlocks.get(i).length);
			byte xordKeyArray[] = xorTwoByteArrays(trBlocks.get(i),keyArray);
			int xorIndex = 0; //to iterate through the array as we rebuild
			for (int j = i; j+keySize < bytes.length; j+=keySize) {
				decoded[j] = xordKeyArray[xorIndex];
				xorIndex++;
			}
		}
		
		String decodedStr = "";
		try {
			decodedStr = new String(decoded, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.print(decodedStr);
		for (int i = 0; i < decoded.length; i++) {
			System.out.println((int)decoded[i]);
		}
		return decodedStr;
		//System.out.println(decodedStr);
	}
		

	/**
	 * Given a byte array and a key size, break up the byte array 
	 * @param b
	 * @param keySize
	 * @return
	 */
	public static ArrayList<byte[]> breakUpIntoBlocks(byte[] bytes, int size){
		
		ArrayList<byte[]> blocks = new ArrayList<byte[]>();
		for (int i = 0; i+size < bytes.length;i+=size) {
			byte block[] = new byte[size];
			for (int j = 0; j < size; j++) {
				block[j] = bytes[i+j];
			}
			blocks.add(block);
		}
		
		return blocks;
	}
	
	public static void printBlocks(ArrayList<byte[]> blocks) {
		System.out.println("Printing as blocks");
		for (int i = 0; i < blocks.size(); i++) {
			System.out.println(byteArrayToBinString(blocks.get(i)));
		}
	}
	
	public static ArrayList<byte[]> transposeBlocks(ArrayList<byte[]> blocks) {
		ArrayList<byte[]> tr = new ArrayList<byte[]>();
		
		int keySize = blocks.get(0).length;
		for (int i = 0; i < keySize; i++) {
			byte[] keyByte = new byte[blocks.size()];
			for (int j = 0; j < keyByte.length;j++) {
				keyByte[j] = blocks.get(j)[i];
			}
			tr.add(keyByte);
		}
		
		return tr;
	}
	
	public static void printTransposedBlocks(ArrayList<byte[]> tr) {
		System.out.println("Printing as transposed blocks");
		for (int i = 0; i < tr.size(); i++) {
			System.out.println(byteArrayToBinString(tr.get(i)));
		}
	}
	
	
	/**
	 * Given a string, that is, a file name, referring to a file comprised of base 64 data
	 * that is encrypted with a repeating XOR cipher, return a string, that is the file decoded. 
	 * @param data the file name
	 * @return the decoded data
	 */
	public static String decodeRepeatingXOR(String fileName) {
		String decoded = "";
		String b64Data = strListToOneString(fileToStringList(fileName));
		int keySize = getKeySize(b64Data);
		byte bytes[] = Base64.getDecoder().decode(b64Data);
		decoded = getKeyAndDecode(bytes, keySize);

		//System.out.println(decoded);
		
		return "";
	}
	
	
	public static void printBigData(String s) {
		for (int i = 0; i < (s.length()%20+1); i++) {
			System.out.println(s.substring(i*20, i*20+20) + "\n");
		}
	}
	
	private static double gaussDistr(double avg) {
		
		double root = 1 / Math.sqrt(2*Math.PI);
		double exp = 0-(avg * avg)/2;
		
		return Math.pow(root, exp);
	}
}
