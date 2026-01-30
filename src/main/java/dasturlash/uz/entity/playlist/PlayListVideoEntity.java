package dasturlash.uz.entity.playlist;

import dasturlash.uz.entity.video.VideoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "play_list_video")
public class PlayListVideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "playlist_id")
    private String playListId;
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    @ManyToOne
    private PlayListEntity playList;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;
    @Column(name = "order_number")
    private Integer orderNumber;
}
