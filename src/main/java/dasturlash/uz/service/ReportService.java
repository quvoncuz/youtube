package dasturlash.uz.service;

import dasturlash.uz.dto.ReportDTO;
import dasturlash.uz.dto.ReportInfo;
import dasturlash.uz.entity.ReportEntity;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.ReportRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public void create(ReportDTO dto){
        String profileId = SpringSecurityUtil.currentProfileId();
        Optional<ReportEntity> reportCheck = reportRepository
                .findReportEntityByProfileIdAndReportedId(profileId, dto.getReportedId());
        if (reportCheck.isPresent()){
            if (reportCheck.get().getCreatedDate().plusMinutes(15).isAfter(LocalDateTime.now())){
                throw new AppBadException("Please try later");
            }
        }

        ReportEntity report = new ReportEntity();
        report.setContent(dto.getContent());
        report.setProfileId(profileId);
        report.setReportedId(dto.getReportedId());
        report.setType(dto.getType());
        reportRepository.save(report);

    }

    public Page<ReportInfo> getReportList(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return reportRepository.findAllReports(pageRequest);
    }

    public void deleteReport(String reportId){
        reportRepository.deleteById(reportId);
    }

    public List<ReportInfo> reportsByProfileId(String profileId){
        return reportRepository.findReportsByProfileId(profileId);
    }
}