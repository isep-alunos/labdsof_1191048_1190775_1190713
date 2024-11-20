package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.requests.CreateIssueRequest;


public interface IssueService {

    void create(CreateIssueRequest createIssueRequest) throws Exception;
}
