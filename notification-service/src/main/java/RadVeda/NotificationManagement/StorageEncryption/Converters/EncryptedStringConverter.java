package RadVeda.NotificationManagement.StorageEncryption.Converters;

import RadVeda.NotificationManagement.StorageEncryption.EncryptionUtility;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * WHAT IT IS:
 * This class is a JPA Attribute Converter that automatically handles the
 * encryption
 * and decryption of String fields when they are saved to or read from the
 * database.
 * 
 * WHY WE NEED IT:
 * We want the encryption process to be transparent to our application logic.
 * Instead of manually calling EncryptionUtility.encrypt() every time we save a
 * sensitive string,
 * we want JPA (Hibernate) to do it for us automatically.
 * 
 * WHY WE NEED IT ALL OF THAT:
 * 1. @Converter: This annotation tells JPA that this class is a converter that
 * can be applied
 * to entity attributes.
 * 2. AttributeConverter<String, String>: We implement this interface to define
 * how to convert
 * between the entity attribute type (String) and the database column type
 * (String).
 * - The first 'String' is the type in our Java class.
 * - The second 'String' is the type stored in the database (which will be the
 * Base64 encrypted string).
 * 3. convertToDatabaseColumn: This method is called before saving to the
 * database. We use our
 * EncryptionUtility to encrypt the plain text.
 * 4. convertToEntityAttribute: This method is called when reading from the
 * database. We use our
 * EncryptionUtility to decrypt the stored value back into plain text.
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : EncryptionUtility.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EncryptionUtility.decrypt(dbData);
    }
}
