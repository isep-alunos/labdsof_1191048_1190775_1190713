package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.requests.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;


public interface IssueService {

    AnalyzeIssuesResponse create(CreateIssueRequest createIssueRequest) throws Exception;
}
