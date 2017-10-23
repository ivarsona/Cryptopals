/**
 * Cryptopals challenge solutions by Drew Ivarson
 * @author drew
 *
 */

import java.io.UnsupportedEncodingException;

import java.util.Scanner;
import java.util.Base64;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class main {
	
	public static final int MAX_BYTE = 255;
	
	public static void main(String[] args) {
		System.out.println("Welcome to Drew's Cyrptopals "
			+ "challenge solutions."
			+ "\nEnter at your own risk.");
	
		
		//challenge1Tests();
		//challenge2Tests();
		//challenge3Tests();
		/*
		try {
			challenge4Tests();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		challenge5Tests();
		challenge6Tests();
		challenge7Tests();

		*/

		challenge8Tests();
		
		
	}		
	
	
	public static void challenge1Tests() {
		System.out.println("\n------Testing challenge 1------\n");
		System.out.println("Test 1: Convert input string to byte array\n");
		String hexString = "49276d206b696c6c696e6720796f757220627261696e2"
				+ "06c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		System.out.println("Original string: " + hexString);
		byte bytes[] = hexOperations.hexStringtoByteArray(hexString);
		hexOperations.printByteArray(bytes, 16);
		String sAB = hexOperations.hexArrayTo64String(bytes);
		System.out.println("Result: " + sAB);
		System.out.println("Answer....: " + "SSdtIGtpbGxpbmcgeW91ciBicm"
				+ "FpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t");
		System.out.print("Answer = result? ... ");
		System.out.println("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t".equals(sAB));	
		}


	public static void challenge2Tests() {
		System.out.println("\n-----Testing challenge 2-----\n");
		System.out.println("Given two hex strings, XOR them, and return a new string of the result");
		String s1 = "1c0111001f010100061a024b53535009181c";
		String s2 = "686974207468652062756c6c277320657965";
		String ans = "746865206b696420646f6e277420706c6179";
		byte b1[] = hexOperations.hexStringtoByteArray(s1);
		byte b2[] = hexOperations.hexStringtoByteArray(s2);
		byte ansA[] = hexOperations.xorTwoByteArrays(b1, b2);
		System.out.println("Answer:\n" + ans +"\n Result: ");
		hexOperations.printByteArray(ansA, 16);
		System.out.println("Answer = result? ... " + hexOperations.byteArrayToString(ansA, 16).equals(ans) );
	}
	
	public static void challenge3Tests() {
		System.out.println("Challenge 3......");
		System.out.println("Given a hex string that has been XOR'd by a single byte");
		System.out.println("Find the key, and decode the string");
		
		String dS = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		System.out.println("Encrypted String: " + dS);
		byte bytes[] = hexOperations.hexStringtoByteArray(dS);
		int len = bytes.length;
		
		//System.out.println("Original:\n" + dS);
		//System.out.println("Generated XOR string: ");
		//hexOperations.printByteArray(testB, 16);
		//System.out.println("XOR'd result: ");
		//hexOperations.printByteArray(xorTest, 16);
		String dSAsAscii = "";
		try {
			dSAsAscii = new String(bytes, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String xorResults[] = new String[MAX_BYTE];
		for (int i = 0; i < MAX_BYTE; i++) {
			byte b = (byte)i;
			byte testB[] = hexOperations.arrayOfByteValAndLen(b, len);
			byte xorTest[] = hexOperations.xorTwoByteArrays(bytes, testB);
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
		}
	
		int scores[] = new int[MAX_BYTE];
		int score_max = 0;
		String bestScore = "";
		for (int i = 0; i < xorResults.length; i++) {
			//scores[i] = hexOperations.getLetterScore(xorResults[i]);
			if (scores[i] > score_max) {
				score_max = scores[i];
				bestScore = xorResults[i];
			}
		}
		
		//System.out.println("Best score: " + score_max);
		System.out.println("Decoded String: " + bestScore);
	}
	
	public static void challenge4Tests() throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("Challenge4Raw.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String> all = new ArrayList<String>();
		ArrayList<byte[]> asBytes = new ArrayList<byte[]>();
		String current = "";
		//System.out.println("Raw: ");
		while (reader.ready()) {
			current = reader.readLine();
			//System.out.println(current);
			all.add(current);
			asBytes.add(hexOperations.hexStringtoByteArray(current));
		}
		
		System.out.println(hexOperations.getBestScore(asBytes));
		//System.out.println("Ascii: ");
		ArrayList<String> asAscii = new ArrayList<String>();
		for (int i = 0; i < asBytes.size(); i++) {
			try {
				asAscii.add(new String(asBytes.get(i), "US-ASCII"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}
	
	public static void challenge5Tests() {
		String key = "ICE";
		String answer = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" + 
				"a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
		String text = "Burning 'em, if you ain't quick and nimble\n" + 
				"I go crazy when I hear a cymbal";
		String result = hexOperations.repeatingKeyXORFromStrings(text, key);
		System.out.println("Result: " + result);
		System.out.println("Answer: " + answer);
		System.out.println("Result length: " + result.length());
		System.out.println("Answer length: " + answer.length());
		System.out.println("Result =? answer: " + answer.equals(result));
		
	}
	
	public static void challenge6Tests() {
		
		System.out.println("Challenge 6...........");
		System.out.println("A file (not shown), is encrypted using a repeating XOR cipher.");
		System.out.println("Here is (hopefully someday) the text, decrypted.\n\n");
		/*
		String s1 = "this is a test";
		String s2 = "wokka wokka!!!";
		String s1Bin = hexOperations.asciiStrToBinString(s1);
		String s2Bin = hexOperations.asciiStrToBinString(s2);
		
		System.out.println(s1Bin);
		System.out.println(s2Bin);
		System.out.println("Hamming distance: " + hexOperations.hammingDistance(s1Bin, s2Bin));
		*/
		String fileName = "6";
		System.out.println(hexOperations.decodeRepeatingXOR(fileName));
	}
	
	public static void challenge7Tests() {
		System.out.println("Challenge 7...........");
		System.out.println("A file (not shown), is encrypted using AES-128-ECB. \nThe key is : "
				+ "YELLOW SUBMARINE");
		System.out.println("Here is (hopefully someday) the text decrypted.\n\n");
		
		System.out.println(Decryptors.aes("7", "YELLOW SUBMARINE", 64));
	}
	
	public static void challenge8Tests() {
		System.out.println("Bits of the file that are encrypted with AES-128 in ECB mode: ");
		System.out.println("\n\n" + Detection.detectAESWithECB("8", 16));
	}
}




