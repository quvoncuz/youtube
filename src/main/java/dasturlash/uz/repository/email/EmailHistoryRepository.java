package dasturlash.uz.repository.email;

import dasturlash.uz.entity.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistoryEntity, String> {

    Optional<EmailHistoryEntity> findByToEmailOrderByCreatedDateDesc(String toEmail);

    Page<EmailHistoryEntity> findAllByToEmail(String toEmail, Pageable pageable);
}
