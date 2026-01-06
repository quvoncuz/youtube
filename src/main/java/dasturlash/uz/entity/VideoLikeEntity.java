package dasturlash.uz.entity;

import dasturlash.uz.enums.Emotion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video_likes")
public class VideoLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column
    private Emotion emotion;
}
