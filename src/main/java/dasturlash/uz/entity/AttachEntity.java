package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attach")
@Getter
@Setter
public class AttachEntity {
    @Id
    private String id;
    @Column(name = "origin_name")
    private String originName;
    @Column
    private Long size;
    @Column
    private String extension;
    @Column
    private String path;
    @Column
    private Integer duration;
}
