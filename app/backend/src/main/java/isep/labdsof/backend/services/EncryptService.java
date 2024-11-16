package isep.labdsof.backend.services;

public interface EncryptService {

    String encrypt(String plaintext) throws Exception;
    String decrypt(String ciphertext) throws Exception;
}
