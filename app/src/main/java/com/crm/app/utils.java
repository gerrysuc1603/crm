package com.crm.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class utils {
    public static String getAppNameFromPackage(Context context, String packageName, boolean returnNull) {
        final PackageManager pm = context.getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        if (returnNull) {
            return ai == null ? null : pm.getApplicationLabel(ai).toString();
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : packageName);
    }

    public static Drawable getAppIconFromPackage(Context context, String packageName) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        Drawable drawable = null;
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            if (ai != null) {
                drawable = pm.getApplicationIcon(ai);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static String nullToEmptyString(CharSequence charsequence) {
        if (charsequence == null) {
            return "";
        } else {
            return charsequence.toString();
        }
    }

    public static String getTime(Long currentTimeMillis){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date resultdate = new Date(currentTimeMillis);
        String currentTime = df.format(resultdate);
        return currentTime;
    }

    public static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String currentTime = df.format(now);
        return currentTime;
    }

    public static String getDate(Long currentTimeMillis){
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        Date resultdate = new Date(currentTimeMillis);
        String currentDate = df.format(resultdate);
        return currentDate;
    }

    public static String encrypt(String plaintext, String passphrase) {
        try {
            final int keySize = 256;
            final int ivSize = 128;

            // Create empty key and iv
            byte[] key = new byte[keySize / 8];
            byte[] iv = new byte[ivSize / 8];

            // Create random salt
            byte[] saltBytes = generateSalt(8);

            // Derive key and iv from passphrase and salt
            EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

            // Actual encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] cipherBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));

            /**
             * Create CryptoJS-like encrypted string from encrypted data
             * This is how CryptoJS do:
             * 1. Create new byte array to hold ecrypted string (b)
             * 2. Concatenate 8 bytes to b
             * 3. Concatenate salt to b
             * 4. Concatenate encrypted data to b
             * 5. Encode b using Base64
             */
            byte[] sBytes = "Salted__".getBytes("UTF-8");
            byte[] b = new byte[sBytes.length + saltBytes.length + cipherBytes.length];
            System.arraycopy(sBytes, 0, b, 0, sBytes.length);
            System.arraycopy(saltBytes, 0, b, sBytes.length, saltBytes.length);
            System.arraycopy(cipherBytes, 0, b, sBytes.length + saltBytes.length, cipherBytes.length);

            byte[] base64b = android.util.Base64.encode(b, android.util.Base64.DEFAULT);

            return new String(base64b);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Decrypt
     * Thanks Artjom B. for this: http://stackoverflow.com/a/29152379/4405051
     * @param ciphertext encrypted string
     * @param passphrase passphrase
     */
    public static String decrypt(String ciphertext, String passphrase) {
        try {
            final int keySize = 256;
            final int ivSize = 128;

            // Decode from base64 text

            byte[] ctBytes = android.util.Base64.decode(ciphertext.getBytes("UTF-8"), android.util.Base64.DEFAULT);

            // Get salt
            byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);

            // Get ciphertext
            byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

            // Get key and iv from passphrase and salt
            byte[] key = new byte[keySize / 8];
            byte[] iv = new byte[ivSize / 8];
            EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

            // Actual decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);

            return new String(recoveredPlaintextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unused")
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * @return a new pseudorandom salt of the specified length
     */
    private static byte[] generateSalt(int length) {
        Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize = keySize / 32;
        ivSize = ivSize / 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            block = hasher.digest(salt);
            hasher.reset();

            // Iterations
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                    Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

            numberOfDerivedWords += block.length / 4;
        }

        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

        return derivedBytes; // key + iv
    }
}
