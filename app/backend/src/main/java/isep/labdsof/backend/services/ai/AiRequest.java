package isep.labdsof.backend.services.ai;

import org.springframework.http.HttpEntity;

public interface AiRequest {

     HttpEntity createHttpEntity();
     String getEndpoint();
     void readResponse(String response);
}
