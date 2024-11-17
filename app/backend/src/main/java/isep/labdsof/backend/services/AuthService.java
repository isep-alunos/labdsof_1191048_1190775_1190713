package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.RegisterUserDto;

public interface AuthService {
    RegisterUserDto register(final String email, final String name, final boolean isEventWorker,
                             final String eventWorkerInfo, final boolean isEventManager, final String eventManagerInfo);
}
