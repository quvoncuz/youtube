package dasturlash.uz.dto.comment;

import dasturlash.uz.dto.video.VideoDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private String id;
    private String content;
    private String replyId;
    private VideoDTO video;
    private String profileId;
    private Long likeCount;
    private Long dislikeCount;
}
