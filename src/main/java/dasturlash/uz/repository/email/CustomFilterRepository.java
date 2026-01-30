package dasturlash.uz.repository.email;

import dasturlash.uz.dto.email.EmailFilterRequestDTO;
import dasturlash.uz.dto.email.FilterResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomFilterRepository {

    @Autowired
    private EntityManager entityManager;

    public FilterResult<Object[]> filterEmailHistory(EmailFilterRequestDTO dto, int page, int size) {

        StringBuilder selectClause = new StringBuilder("""
                SELECT  e.id,
                        e.toEmail,
                        e.code,
                        e.message
                        FROM EmailHistoryEntity e
                """);
        StringBuilder countClause = new StringBuilder("SELECT COUNT(e.id) FROM EmailHistoryEntity e");
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");

        Map<String, Object> parameters = new HashMap<>();

        if (!dto.getEmail().isBlank()){
            whereClause.append(" and e.toEmail = :email ");
            parameters.put("email", dto.getEmail());
        }

        if ( dto.getFromDate() != null){
            LocalDateTime fromDate = LocalDateTime.of(dto.getFromDate(), LocalTime.MIN);
            whereClause.append(" and e.createdDate >= :fromDate");
            parameters.put("fromDate", fromDate);
        }

        if ( dto.getToDate() != null){
            LocalDateTime toDate = LocalDateTime.of(dto.getToDate(), LocalTime.MAX);
            whereClause.append(" and e.createdDate < :toDate");
            parameters.put("toDate", toDate);
        }

        selectClause.append(whereClause);
        countClause.append(whereClause);

        Query selectQuery = entityManager.createQuery(selectClause.toString());
        Query countQuery = entityManager.createQuery(countClause.toString());

        parameters.forEach(selectQuery::setParameter);

        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);

        List<Object[]> resultList = selectQuery.getResultList();

        parameters.forEach(countQuery::setParameter);
        Long totalCount = (Long) countQuery.getSingleResult();

        return new FilterResult<>(resultList, totalCount);
    }
}
