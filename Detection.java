import java.util.ArrayList;
public class Detection {

	
	/**
	 * Detects blocks within a file that are likely to be encrypted with AES-128 in ECB mode.
	 * 
	 * The file can either be in base 64 or in hexadecimal.
	 * 
	 * @param fileName the name of the file
	 * @param base the base of the information
	 * @return a string containing the parts of the file that are encrypted with AES-128 in ECB mode.
	 */
	public static String detectAESWithECB(String filename, int base) {
		
		String stuff = "";
		byte bytes[] = null;
		if (base == 64) {
			bytes = FileDecoder.parseBase64FileToByteArray(filename);
		}
		else if (base == 16) {
			bytes = FileDecoder.parseHexFileToByteArray(filename);
		}
		else {
			System.out.println("File not in base 16 or 64. Wtf mate...");
			return "POOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP";
		}
		System.out.println("There are : " + bytes.length/16 + " 16-byte blocks of data in this file.");
		//String asAscii = new String(asBytes);
		//System.out.println(asAscii);
		
		//from the contents as a byte array, find the set of strings for whom
		//the total sum of hamming distances is minimized
		return stuff;
	}
	
	public static byte[] findMinHammingSet(byte[] bytes) {
		byte[] toRet = null;
		ArrayList<int[]> dists = new ArrayList<int[]>(); 
		
		int blockSize = 16; //number of bytes in a block
		int numBlocks = bytes.length/blockSize; //number of blocks total
		int blockOffset = 1; //size of offset, to be iterated over
		int start = 0; //block to start with, will try all
		//take all possible offsets and all possible starts, and find the lowest average hamming distance given each set
		int averages[] = new int[numBlocks]; //save averages to array, there should be numBlocks of them
		//start at the beginning, increase by block size*block offset
		int averagesCount = 0; //separate counter to iterate over just the list of averages
		for (start = 0; start+(blockSize+blockOffset) < bytes.length; start+=(blockSize*blockOffset)) {
			//for ()
		}
		for (blockOffset = 1; blockOffset < numBlocks; blockOffset++) {
			for (start = 0; start+(blockSize*blockOffset) < bytes.length; start+=(blockSize*blockOffset)) {
				
			}
		}
		int subListCount = 0; //helper counter for grabbing 16-byte blocks
		int distListCount = 0; //helper counter to generate lists of distances starting from each block
		for (int i = 0; i+blockSize < bytes.length-1; i+=blockSize) {
			int currentDists[] = new int[(bytes.length-i)/16];
			byte b1[] = new byte[16]; //grab the i-th 16-byte block
			for (int k = i; k < i + 16; k++) {
				b1[subListCount] = bytes[k];
			}
			subListCount = 0;
			String b1Bin = hexOperations.byteArrayToBinString(b1);
			for (int j = i+16; j+16 < bytes.length-1; j+=16) { //now compare it to all following 16-byte blocks
				byte b2[] = new byte[16]; //grab the j-th 16-byte block
				for (int k = j; k < j + 16; k++) {
					b2[subListCount] = bytes[k];
				}
				subListCount = 0;
				String b2Bin = hexOperations.byteArrayToBinString(b2);
				//now that we have both 16-byte blocks as binary strings, get the hamming distance between them
				//then dump said hamming distance into an array of all distances between block i and the end
				currentDists[distListCount] = hexOperations.hammingDistance(b1Bin, b2Bin);
				
				distListCount++;
			}
			distListCount = 0;
			dists.add(currentDists); 
		}
		
		
		
		
		
		return toRet;
	}
	
	
}
