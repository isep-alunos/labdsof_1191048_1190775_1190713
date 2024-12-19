package isep.labdsof.backend.services.implementations;


import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.responses.LeaderboardEntryDto;
import isep.labdsof.backend.domain.responses.LeaderboardResponse;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final IssueRepository issueRepository;

    @Override
    public UserProfile getByUserId(UUID id) throws LabdsofCustomException {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(id);
        if (userProfileOpt.isEmpty()) {
            throw new LabdsofCustomException(AppCustomExceptions.ENTITY_NOT_FOUND, "User with id " + id + " not found");
        }
        return userProfileOpt.get();
    }

    @Override
    public UserProfile getByUserEmail(final String email) throws LabdsofCustomException {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUser_Email(email);
        if (userProfileOpt.isEmpty()) {
            throw new LabdsofCustomException(AppCustomExceptions.ENTITY_NOT_FOUND, "User with email " + email + " not found");
        }
        return userProfileOpt.get();
    }

    @Override
    public LeaderboardResponse getLeaderboard() {
        final List<LeaderboardEntryDto> top10MostPointsAvailable = userProfileRepository.getTopMostPointsAvailable(PageRequest.of(0,10));
        final List<LeaderboardEntryDto> top10MostReportedIssuesResolved = issueRepository.getTopReportedResolvedIssues(PageRequest.of(0, 10));

        return new LeaderboardResponse(top10MostPointsAvailable, top10MostReportedIssuesResolved);
    }
}