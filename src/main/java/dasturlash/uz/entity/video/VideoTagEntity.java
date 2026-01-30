package dasturlash.uz.entity.video;

import dasturlash.uz.entity.TagEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video_tags")
public class VideoTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "video_id", nullable = false)
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "tag_id", nullable = false)
    private Integer tagId;
    @ManyToOne
    @JoinColumn(name = "tag_id", updatable = false, insertable = false)
    private TagEntity tag;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;
}
