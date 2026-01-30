package dasturlash.uz.repository;

import dasturlash.uz.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    @Query("from ProfileEntity where email = ?1")
    Optional<ProfileEntity> findByEmail(String email);
}
