package dasturlash.uz.dto.video;

import dasturlash.uz.enums.StatusEnum;

import java.time.LocalDateTime;

public interface VideoFullInfo {
    String getId();
    String getTitle();
    String getDescription();
    StatusEnum getStatus();
    String getPreviewAttachId();
    String getPreviewAttachUrl();
    String getAttachId();
    String getAttachUrl();
    String getAttachDuration();
    Integer getCategoryId();
    String getCategoryName();
    String getTagList(); // JSON
    LocalDateTime getPublishedDate();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoUrl();
    Long getViewCount();
    Long getSharedCount();
    Long getLikeCount();
    Long getDislikeCount();
    Boolean getIsUserLiked();
    Boolean getIsUserDisliked();
}
