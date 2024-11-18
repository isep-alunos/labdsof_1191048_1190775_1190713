package isep.labdsof.backend.services;



import isep.labdsof.backend.domain.models.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getById(UUID id) throws Exception;
    List<User> getEventWorkers();
}
