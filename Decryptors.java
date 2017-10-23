import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;


public class Decryptors {

	public static String aes(String fileName, String key, int base) {
		
		String contents = hexOperations.strListToOneString(hexOperations.fileToStringList(fileName));
		
		
		byte[] bytes = null;
		if (base == 64 ) {
			bytes = Base64.getDecoder().decode(contents);
		}
		else if (base == 16) {
			bytes = hexOperations.hexStringtoByteArray(contents);
		}
		
		Cipher c = null;
		try {
			c = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecretKey myKey = new SecretKeySpec(hexOperations.asciiStrToByteArray(key), 0, 16, "AES");
		//Key myKey = hexOperations.asciiStrToByteArray(key);
		try {
			c.init(Cipher.DECRYPT_MODE, myKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] recovered = null;
		try {
			recovered = c.doFinal(bytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String toRet = new String(recovered);
		return toRet;
	}
}
