package dev.goltsov.shortlinksgenerator.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

public class PasswordUtils {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String password, String storedPassword) {
        return BCrypt.checkpw(password, storedPassword);
    }
}
