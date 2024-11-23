package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;

import java.util.List;


public interface IssueService {

    AnalyzeIssuesResponse create(CreateIssueRequest createIssueRequest) throws Exception;
    List<IssueDto> getIssuesByEventName(String eventName) throws Exception;
}
