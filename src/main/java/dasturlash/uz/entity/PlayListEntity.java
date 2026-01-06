package dasturlash.uz.entity;

import dasturlash.uz.enums.PlaylistStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "play_list")
public class PlayListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private PlaylistStatus status;

    @Column(name = "order_number")
    private Integer orderNumber;
}
