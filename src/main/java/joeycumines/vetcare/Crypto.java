package joeycumines.vetcare;

import java.security.SecureRandom;
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Crypto {
	private static final SecureRandom random = new SecureRandom();

	/**
	 * http://stackoverflow.com/a/41156
	 * 
	 * @return
	 */
	public static String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	/**
	 * http://stackoverflow.com/a/33085670
	 * 
	 * Returns null if it fails.
	 * 
	 * @param _passwordToHash
	 * @param _salt
	 * @return
	 */
	public static String get_SHA_512_SecurePassword(String _passwordToHash, String _salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(_salt.getBytes("UTF-8"));
			byte[] bytes = md.digest(_passwordToHash.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (Exception e) {
			return null;
		}
		return generatedPassword;
	}
	
	/**
	 * Returns null if it fails.
	 * @param _input
	 * @return
	 */
	public static String toBase64(String _input) {
		try {
			return Base64.getEncoder().encodeToString(_input.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * Returns null if it fails.
	 * @param _input
	 * @return
	 */
	public static String fromBase64(String _input) {
		byte[] asBytes = Base64.getDecoder().decode(_input);
		try {
			return new String(asBytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
