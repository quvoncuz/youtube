package dasturlash.uz.dto;

import dasturlash.uz.enums.ReportType;
import lombok.Data;

@Data
public class ReportDTO {
    private String id;
    private String profileId;
    private String content;
    private ReportType type;
    private String reportedId;
}
