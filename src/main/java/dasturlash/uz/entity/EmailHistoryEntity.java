package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "to_email")
    private String toEmail;

    @Column
    private String title;

    @Column
    private String message;

    @Column
    private String code;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
