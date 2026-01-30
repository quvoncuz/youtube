package dasturlash.uz.entity.comment;

import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.entity.video.VideoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column
    private String content;

    @Column(name = "reply_id")
    private String replyId;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
