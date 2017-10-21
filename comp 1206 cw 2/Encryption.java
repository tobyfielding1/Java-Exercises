

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

class Encryption {
	private final String transformation = "AES";
	//default hardcoded key, important to store data away from program as hacker could just read key here (although client uses new Key later)
	private byte[] key = new String("MyDifficultPassw").getBytes();

	public void encrypt(Serializable object, OutputStream ostream) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
		encrypt(object, ostream, key);
	}
	
	public void encrypt(Serializable object, OutputStream ostream, byte[] cipherKey) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(cipherKey, transformation);

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        SealedObject sealedObject;
		try {
			sealedObject = new SealedObject(object, cipher);
	        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
	        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
	        outputStream.writeObject(sealedObject);
	        outputStream.close();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
	}

	public Object decrypt(InputStream istream, byte[] cipherKey) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
	    if(cipherKey == null)
	    	cipherKey = key;
	    
		SecretKeySpec sks = new SecretKeySpec(cipherKey, transformation);
	    Cipher cipher = Cipher.getInstance(transformation);
	    cipher.init(Cipher.DECRYPT_MODE, sks);

	    CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
	    ObjectInputStream inputStream = null;
	    try {
	    	inputStream = new ObjectInputStream(cipherInputStream);
	    	SealedObject sealedObject;
	        sealedObject = (SealedObject) inputStream.readObject();
	        inputStream.close();
	        return sealedObject.getObject(cipher);
	    } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e ) {
	        e.printStackTrace();
	        return null;
	    } catch (java.io.EOFException e){
			//System.out.println("premature read, repeating de-serialization of " );
			if (inputStream != null)
				inputStream.close();
			//e.printStackTrace();
			return null;
		}
	}
	
	public Object decrypt(InputStream istream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		return decrypt(istream,null);
	}
	
	public String newKey(){
		String str = randomString(16);
		key = str.getBytes();
		return str;
	}
	
	// used to generate new secure key
	public String randomString(int n) {
        String CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < n) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            salt.append(CHARS.charAt(index));
        }
        String str = salt.toString();
        return str;

    }

	public byte[] getKey() {
		return key;
	}
}
