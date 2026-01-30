package dasturlash.uz.dto.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.dto.ChannelDTO;
import dasturlash.uz.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {
    private String id;
    private String title;
    private String description;
    private CategoryDTO category;
    private AttachDTO attachDTO;
    private String previewVideoId;
    private ChannelDTO channelDTO;
    private StatusEnum status;
    private Long likeCount;
    private Long dislikeCount;
    private Long viewCount;
    private Long sharedCount;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
}
