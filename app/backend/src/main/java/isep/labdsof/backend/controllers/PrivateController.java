package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.requests.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.services.IssueService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private")
public class PrivateController {

    private final IssueService issueService;


    @PostMapping("/create-issue")
    public ResponseEntity<AnalyzeIssuesResponse> createIssue(@RequestBody CreateIssueRequest request) throws Exception {
        AnalyzeIssuesResponse response = issueService.create(request);
        return ResponseEntity.status(201).body(response);
    }

}
