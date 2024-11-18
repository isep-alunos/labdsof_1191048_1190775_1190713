package isep.labdsof.backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class EventWorkersDto {
    String name;
    String email;
    UUID id;
}
