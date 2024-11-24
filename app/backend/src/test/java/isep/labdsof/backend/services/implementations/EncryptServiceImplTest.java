
package isep.labdsof.backend.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class EncryptServiceImplTest {

    private EncryptServiceImpl encryptService;

    @BeforeEach
    void setUp() {
        encryptService = new EncryptServiceImpl();
        ReflectionTestUtils.setField(encryptService, "encryptPassword", "ThisIsASecretKey");
    }

    @Test
    void testEncryptAndDecrypt() throws Exception {
        final String plaintext = "Encrypt me!";
        final String encryptedText = encryptService.encrypt(plaintext);
        final String decryptedText = encryptService.decrypt(encryptedText);

        assertNotNull(encryptedText);
        assertNotEquals(plaintext, encryptedText);
        assertEquals(plaintext, decryptedText);
    }
}