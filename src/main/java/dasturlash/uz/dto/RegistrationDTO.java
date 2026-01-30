package dasturlash.uz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String mainPhoto;
}
