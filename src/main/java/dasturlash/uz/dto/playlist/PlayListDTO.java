package dasturlash.uz.dto.playlist;

import dasturlash.uz.dto.ChannelDTO;
import dasturlash.uz.enums.PlayListStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayListDTO {
    private String id;
    private ChannelDTO channel;
    private String name;
    private String description;
    private PlayListStatus status;
    private Integer orderNumber;
}
