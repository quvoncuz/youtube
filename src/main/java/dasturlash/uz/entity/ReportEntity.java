package dasturlash.uz.entity;

import dasturlash.uz.enums.ReportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "report")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "report_type")
    private String profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column
    private String content;

    // channel, comment, video, profile
    @Column(name = "reported_id")
    private String reportedId;

    @Enumerated(EnumType.STRING)
    @Column
    private ReportType type;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;
}
