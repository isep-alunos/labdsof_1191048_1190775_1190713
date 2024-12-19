package isep.labdsof.backend.services.ai;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AiService {

    public void callAi(AiRequest request, RestTemplate restTemplate) {

        String pythonApiUrl = "http://localhost:8082/" + request.getEndpoint();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl,
                    request.createHttpEntity(), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                request.readResponse(response.getBody());

            } else {
                request.readResponse(null);
            }
        } catch (Exception e) {
            request.readResponse(null);
        }

    }
}
