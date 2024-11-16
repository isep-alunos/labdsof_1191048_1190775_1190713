package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.services.EncryptService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Service
public class EncryptServiceImpl implements EncryptService {

    @Value("${crypt.password}")
    private String encryptPassword;

    public String encrypt(String plaintext) throws Exception {
        Key key = new SecretKeySpec(encryptPassword.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt ciphertext using AES
    public String decrypt(String ciphertext) throws Exception {
        Key key = new SecretKeySpec(encryptPassword.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedCiphertext = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(decodedCiphertext);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
