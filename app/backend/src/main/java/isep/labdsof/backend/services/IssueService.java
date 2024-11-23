package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.requests.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;

import java.util.List;


public interface IssueService {

    AnalyzeIssuesResponse create(CreateIssueRequest createIssueRequest) throws Exception;
    List<IssueDto> getIssuesByEventName(String eventName) throws Exception;
}
