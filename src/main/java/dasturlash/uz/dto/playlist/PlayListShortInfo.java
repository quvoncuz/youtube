package dasturlash.uz.dto.playlist;

import java.time.LocalDateTime;

public interface PlayListShortInfo {
    String getId();
    String getName();
    String getChannelId();
    String getChannelName();
    Integer getVideoCount();
    String getVideoId();
    String getVideoName();
    String getVideoDuration();
    LocalDateTime getCreatedDate();
}
