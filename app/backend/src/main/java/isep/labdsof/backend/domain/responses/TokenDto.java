package isep.labdsof.backend.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String expireDate;
    private String refreshToken;
    private String name;
    private String picture;
    private boolean admin;

    public TokenDto(final String token, final String expireDate) {
        this.token = token;
        this.expireDate = expireDate;
    }
}
