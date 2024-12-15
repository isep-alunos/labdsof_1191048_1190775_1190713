package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ReactToIssueRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import jakarta.validation.Valid;

import java.util.List;


public interface IssueService {

    AnalyzeIssuesResponse create(String userEmail, CreateIssueRequest createIssueRequest) throws LabdsofCustomException;

    List<IssueDto> getIssuesByEventName(String userEmail, String eventName) throws LabdsofCustomException;

    IssueDto reactToIssue(@Valid ReactToIssueRequest request, String email) throws LabdsofCustomException;
    IssueDto praiseToIssue(@Valid ReactToIssueRequest request, String email) throws LabdsofCustomException;
}
