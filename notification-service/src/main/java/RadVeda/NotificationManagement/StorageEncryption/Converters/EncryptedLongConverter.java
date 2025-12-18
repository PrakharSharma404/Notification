package RadVeda.NotificationManagement.StorageEncryption.Converters;

import RadVeda.NotificationManagement.StorageEncryption.EncryptionUtility;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * WHAT IT IS:
 * This class is a JPA Attribute Converter for Long type attributes.
 * 
 * WHY WE NEED IT:
 * Similar to EncryptedStringConverter, we have Long fields (like recipientId)
 * that are sensitive
 * and need to be encrypted in the database.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. AttributeConverter<Long, String>: We convert between Long (entity
 * attribute) and String (database column).
 * The database stores the encrypted value as a Base64 String, even though the
 * original value is a number.
 * 2. EncryptionUtility.encrypt(Long): Uses the overloaded helper method to
 * convert Long -> String -> Encrypted String.
 * 3. EncryptionUtility.decryptLong(String): Uses the helper method to decrypt
 * String -> String -> Long.
 */
@Converter
public class EncryptedLongConverter implements AttributeConverter<Long, String> {

    @Override
    public String convertToDatabaseColumn(Long attribute) {
        return attribute == null ? null : EncryptionUtility.encrypt(attribute);
    }

    @Override
    public Long convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EncryptionUtility.decryptLong(dbData);
    }
}
