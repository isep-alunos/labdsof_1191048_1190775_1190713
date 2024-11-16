package isep.labdsof.backend.convertor;

import isep.labdsof.backend.services.EncryptService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;

@Converter
@AllArgsConstructor
public class StringCryptoConverter implements AttributeConverter<String, String> {

    private final EncryptService encryptor;

    /**
     * Converts the attribute value to the encrypted form.
     *
     * @param attribute The original attribute value to be encrypted.
     * @return The encrypted form of the attribute.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return encryptor.encrypt(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the encrypted database value to its decrypted form.
     *
     * @param dbData The encrypted value stored in the database.
     * @return The decrypted form of the database value.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return encryptor.decrypt(dbData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}