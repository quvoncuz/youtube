package dasturlash.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String photoUrl;
    private Role role;
    private String jwtToken;
}
