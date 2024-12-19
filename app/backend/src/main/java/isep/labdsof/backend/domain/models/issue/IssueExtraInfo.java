package isep.labdsof.backend.domain.models.issue;


import isep.labdsof.backend.domain.models.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class IssueExtraInfo extends BaseEntity {

    private String question;
    private String answer;


}
