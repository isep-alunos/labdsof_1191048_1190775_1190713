package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.requests.AnalyseIssueRequest;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ReactToIssueRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.domain.responses.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.ai.ClarificationResponse;
import jakarta.validation.Valid;

import java.util.List;


public interface IssueService {

    AnalyzeIssuesResponse analyseIssue(String userEmail, AnalyseIssueRequest analyseIssueRequest) throws LabdsofCustomException;
    MessageDto createIssue(String userEmail, CreateIssueRequest createIssueRequest) throws LabdsofCustomException;
    ClarificationResponse getClarificationQuestions(String userEmail, AnalyseIssueRequest analyseIssueRequest) throws LabdsofCustomException;

    List<IssueDto> getIssuesByEventName(String userEmail, String eventName) throws LabdsofCustomException;

    IssueDto reactToIssue(@Valid ReactToIssueRequest request, String email) throws LabdsofCustomException;
    IssueDto praiseToIssue(@Valid ReactToIssueRequest request, String email) throws LabdsofCustomException;
}
