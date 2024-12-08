package isep.labdsof.backend.services.implementations;


import isep.labdsof.backend.domain.dtos.user.UserProfileDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(UUID id) throws LabdsofCustomException {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new LabdsofCustomException(AppCustomExceptions.USER_NOT_FOUND, "User with id " + id + " not found");
        }
        return userOpt.get();
    }

    @Override
    public List<User> getEventWorkers() {
        return userRepository.findAllByRolesContaining(Role.EVENT_WORKER);
    }
}
