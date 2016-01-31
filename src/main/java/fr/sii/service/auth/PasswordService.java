package fr.sii.service.auth;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by tmaugin on 15/05/2015.
 */
public final class PasswordService {
    /**
     * Hash the given plain text password
     * @param plaintext
     * @return hashed password
     */
    public static String hashPassword(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    /**
     * Verify if hashed password and plain text password correspond
     * @param plaintext
     * @param hashed
     * @return Passwords match or not
     */
    public static boolean checkPassword(String plaintext , String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }
}