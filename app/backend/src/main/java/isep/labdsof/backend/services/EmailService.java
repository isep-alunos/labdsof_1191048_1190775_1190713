package isep.labdsof.backend.services;

public interface EmailService {
    void sendMail(String mailAddress, String title, String mailMessage);
}
