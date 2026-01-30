package dasturlash.uz.dto.email;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmailFilterRequestDTO {
    private String email;
    private LocalDate fromDate;
    private LocalDate toDate;
}
