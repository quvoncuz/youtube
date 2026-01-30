package dasturlash.uz.dto;

import dasturlash.uz.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class JwtDTO {
    private String id;
    private String email;
}
