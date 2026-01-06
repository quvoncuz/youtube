package dasturlash.uz.entity;

import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "profile")
@Getter
@Setter
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "photo_id")
    private String photoId;
    @OneToOne
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @Enumerated(EnumType.STRING)
    @Column
    private Role roles;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
}
