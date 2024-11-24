package isep.labdsof.backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class EventWorkersDto {
    public String name;
    public String email;
    public UUID id;
}
