package isep.labdsof.backend.config;

import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Value("${backend.admin-email}")
    private String adminEmail;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup)
            return;
        addAdminUser();
        alreadySetup = true;
    }

    private void addAdminUser() {
        if (userRepository.findUserByEmail(adminEmail).isPresent())
            return;
        final User user = User.builder()
                .nome("Admin")
                .email(adminEmail)
                .roles(Collections.singletonList(Role.ADMIN))
                .isWaitingForApprovalForEventWorker(false)
                .isWaitingForApprovalForEventManager(false)
                .build();
        userRepository.save(user);
    }
}
