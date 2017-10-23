/**
 * This class of helper methods deals with operations on bytes and byte arrays.
 * Most of its features deal with converting to and from byte arrays, though it does
 * have some features which perform calculations on given byte arrays or given 
 * string-encoded bytewise information.
 * @author drew
 *
 */
import java.util.Base64;

public class ByteWiseOps {

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
	 * Given a base 64 string, gives it back as a byte array
	 * @param og64
	 * @return
	 */
	public static byte[] base64StringToByteArray(String og64) {
		return Base64.getDecoder().decode(og64);
	}
	
}
