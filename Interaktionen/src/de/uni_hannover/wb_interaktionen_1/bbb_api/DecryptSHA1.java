package de.uni_hannover.wb_interaktionen_1.bbb_api;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** This class can decrypt a string in SHA-1.
 *
 * @author David Sasse
 */
public class DecryptSHA1 {
    String data;

    /** Constructor for DecryptSHA1
     *
     * @param data The String that has to be decrypted.
     */
    public DecryptSHA1(String data){
        this.data = data;
    }

    /** Decrypts in SHA-1
     *
     * This methode decrypts the string data, that is passed in the constructor, in SHA-1.
     *
     * @return The decrypted string.
     */
    public String decode(){
        byte[] bytes = data.getBytes(StandardCharsets.US_ASCII); //Convert String to bytes array
        byte[] hash = hash(bytes); //Computes has with SHA-1
        return encodeHexString(hash); //Converts byte array back to string
    }

    /** Decrypts with SHA-1
     *
     * This methode decrypts a given byte array in SHA-1.
     *
     * @param hashThis Byte array that has to be decrypted.
     * @return The decrypted byte array.
     */
    private static byte[] hash(byte[] hashThis) {
        try {
            byte[] hash;
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = md.digest(hashThis);
            return hash;
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("SHA-1 algorithm is not available...");
            System.exit(2);
        }
        return null;
    }

    /** Converts a byte array to a hexadecimal string.
     *
     * This methode converts every entry in the byte array to hexadecimal number and
     * returns all the numbers in one string.
     *
     * @param byteArray Byte Array that has to be converted.
     * @return The hexadecimal string.
     */
    private String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    /** Converts a single byte into a hexadecimal.
     *
     * this methode converts a single byte into a hexadecimal number and writes it in a string.
     *
     * @param num Byte that has to be converted.
     * @return The hexadecimal String
     */
    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
