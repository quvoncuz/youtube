package dasturlash.uz.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FilterResult<T> {
    private List<T> content;
    private Long totalElement;
}
