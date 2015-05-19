package fr.sii.service.auth;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by tmaugin on 15/05/2015.
 */
public final class PasswordService {
    public static String hashPassword(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plaintext , String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }
}