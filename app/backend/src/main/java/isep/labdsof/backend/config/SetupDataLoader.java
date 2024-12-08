package isep.labdsof.backend.config;

import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

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
                .roles(List.of(Role.ADMIN,Role.USER, Role.EVENT_WORKER, Role.EVENT_MANAGER))
                .isWaitingForApprovalForEventWorker(false)
                .isWaitingForApprovalForEventManager(false)
                .build();
        userRepository.save(user);

        final UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();
        userProfileRepository.save(userProfile);
    }
}
