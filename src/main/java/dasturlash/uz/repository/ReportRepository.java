package dasturlash.uz.repository;

import dasturlash.uz.dto.ReportInfo;
import dasturlash.uz.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String> {
    Optional<ReportEntity> findReportEntityByProfileIdAndReportedId(String profileId, String reportedId);

    @Query("""
            select r.id as id,
                     r.profileId as profileId,
                     p.name as profileName,
                     p.surname as profileSurname,
                     a.id as profilePhotoId,
                     a.path as profilePhotoUrl,
                     r.content as content,
                     r.reportedId as reportedId,
                     r.type as type
            from ReportEntity as r
            inner join ProfileEntity as p on p.id = r.profileId
            left join AttachEntity as a on a.id = p.photoId
            order by r.createdDate desc
            """)
    Page<ReportInfo> findAllReports(Pageable pageable);

    @Query("""
            select r.id as id,
                     r.profileId as profileId,
                     p.name as profileName,
                     p.surname as profileSurname,
                     a.id as profilePhotoId,
                     a.path as profilePhotoUrl,
                     r.content as content,
                     r.reportedId as reportedId,
                     r.type as type
            from ReportEntity as r
            inner join ProfileEntity as p on p.id = r.profileId
            left join AttachEntity as a on a.id = p.photoId
            where r.profileId = ?1
            order by r.createdDate desc
            """)
    List<ReportInfo> findReportsByProfileId(String profileId);
}
