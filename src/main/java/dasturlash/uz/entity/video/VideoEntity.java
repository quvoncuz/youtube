package dasturlash.uz.entity.video;

import dasturlash.uz.entity.AttachEntity;
import dasturlash.uz.entity.CategoryEntity;
import dasturlash.uz.entity.ChannelEntity;
import dasturlash.uz.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @ManyToOne
    @JoinColumn(name = "preview_attach_id", insertable = false, updatable = false)
    private AttachEntity previewAttach;

    @Column
    private String title;

    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "attach_id")
    private String attachId;
    @OneToOne
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "shared_count")
    private Long sharedCount;

    @Column
    private String description;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;
}
