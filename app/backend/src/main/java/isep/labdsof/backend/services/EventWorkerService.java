package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.requests.UpdateIssueRequest;
import isep.labdsof.backend.domain.responses.EventWorkerIssuesResponse;

import java.util.List;

public interface EventWorkerService {

    List<EventWorkerIssuesResponse> getEventWorkerIssues(final String email);

    IssueDto updateIssue(final String email, final UpdateIssueRequest request) throws LabdsofCustomException;
}
