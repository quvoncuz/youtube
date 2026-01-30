package dasturlash.uz.dto.video;

import java.time.LocalDateTime;

public interface VideoTagMapper {
    String getId();
    String getVideoId();
    Integer getTagId();
    String getTagName();
    LocalDateTime getCreatedDate();
}
