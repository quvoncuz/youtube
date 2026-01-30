package dasturlash.uz.dto.video;

import dasturlash.uz.dto.TagDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoTagDTO {
    private String videoId;
    private TagDTO tag;
}
