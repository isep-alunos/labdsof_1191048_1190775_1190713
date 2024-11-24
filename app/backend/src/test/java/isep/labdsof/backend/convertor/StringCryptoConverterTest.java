
package isep.labdsof.backend.convertor;

import isep.labdsof.backend.services.EncryptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringCryptoConverterTest {

    private EncryptService encryptService;
    private StringCryptoConverter converter;

    @BeforeEach
    public void setUp() {
        encryptService = mock(EncryptService.class);
        converter = new StringCryptoConverter(encryptService);
    }

    @Test
    public void testConvertToDatabaseColumn() throws Exception {
        final String attribute = "test";
        final String encrypted = "encryptedTest";
        when(encryptService.encrypt(attribute)).thenReturn(encrypted);

        final String result = converter.convertToDatabaseColumn(attribute);
        assertEquals(encrypted, result);
    }

    @Test
    public void testConvertToEntityAttribute() throws Exception {
        final String dbData = "encryptedTest";
        final String decrypted = "test";
        when(encryptService.decrypt(dbData)).thenReturn(decrypted);

        final String result = converter.convertToEntityAttribute(dbData);
        assertEquals(decrypted, result);
    }
}