package RadVeda.NotificationManagement.StorageEncryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * WHAT IT IS:
 * This class is a utility helper for handling encryption and decryption
 * operations.
 * 
 * WHY WE NEED IT:
 * We need to protect sensitive data stored in our database. By encrypting it
 * before saving
 * and decrypting it when reading, we ensure that if the database is
 * compromised, the actual
 * data remains secure.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. AES Algorithm: We use AES (Advanced Encryption Standard) because it is a
 * widely accepted
 * and secure symmetric encryption algorithm.
 * 2. Static Block & System.getenv: We initialize the secret key from an
 * environment variable
 * (STORAGE_ENCRYPTION_SECRET_KEY) instead of hardcoding it. This is a critical
 * security practice
 * to prevent secrets from being committed to version control.
 * 3. Cipher Class: This is Java's standard class for cryptographic operations.
 * We perform both
 * encryption (ENCRYPT_MODE) and decryption (DECRYPT_MODE).
 * 4. Base64: Encryption produces raw bytes, which can be messy to store or
 * print. Base64 encoding
 * converts these bytes into a safe, printable string format.
 */
public class EncryptionUtility {

    private static final String ALGORITHM = "AES";
    private static final SecretKey SECRET_KEY;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    static {
        try {
            // IMPORTANT: You must set this environment variable on your machine/server
            String key = System.getenv("STORAGE_ENCRYPTION_SECRET_KEY");
            if (key == null || key.length() != 16) {
                throw new IllegalStateException(
                        "Storage encryption secret key is not properly configured! Must be 16 characters.");
            }
            SECRET_KEY = new SecretKeySpec(key.getBytes(), ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing encryption utility", e);
        }
    }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    public static String encrypt(Long value) {
        return encrypt(value.toString());
    }

    public static Long decryptLong(String encryptedText) {
        return Long.parseLong(decrypt(encryptedText));
    }
}
