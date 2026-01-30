package dasturlash.uz.dto.comment;

import java.time.LocalDateTime;

public interface CommentMapper {
    String getId();
    String getContent();
    String getVideoId();
    String getPreviewVideoId();
    String getTitle();

    Long getLikeCount();
    Long getDislikeCount();
    LocalDateTime getCreatedDate();
}
