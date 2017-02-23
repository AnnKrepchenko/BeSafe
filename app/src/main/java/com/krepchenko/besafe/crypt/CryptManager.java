package com.krepchenko.besafe.crypt;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

/**
 * Created by Ann on 18.10.2015.
 */
public class CryptManager {

    private static final String passForPass = "gUk9a5Qsv_besafe_dnMLFz";
    private static final String passPrefics = "chechethib";
    private static final String passSuffics = "ocZSRUkI8c";
    private static final String secretField = "rabbit";

    public static String getEncryptedPass(String password){
        String result = "";
        StringBuilder builder = new StringBuilder();
        builder.append(password);
        builder=builder.reverse();
        password = builder.toString();
        try {
            result = AESCrypt.encrypt(passForPass, passPrefics + password + passSuffics);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encrypt(String password,String data) {
        String result = "";
        try {
            result = AESCrypt.encrypt(password, data);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decrypt(String password,String data) {
        String result = "";
        try {
            result = AESCrypt.decrypt(password, data);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getSecretField(String password) {
        String result = "";
        try {
            result = AESCrypt.encrypt(password, secretField);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }


}
