package dasturlash.uz.repository.playlist;

import dasturlash.uz.dto.playlist.PlayListInfo;
import dasturlash.uz.entity.playlist.PlayListEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListRepository extends JpaRepository<PlayListEntity, String> {

    @Modifying
    @Transactional
    @Query("update PlayListEntity set status = ?1 where id = ?2")
    int updateStatus(String status, String id);

    @Query("""
                    select pl.id as id,
                           pl.name as name,
                           pl.description as description,
                           pl.status as status,
                           pl.orderNumber as orderNumber,
                           pl.channelId as channelId,
                           pl.channel.name as channelName,
                           pl.channel.photoId as channelPhotoId,
                           pl.channel.photo.path as channelPhotoUrl,
                           pl.channel.profileId as profileId,
                           pl.channel.profile.name as profileName,
                           pl.channel.profile.surname as profileSurname,
                           pl.channel.profile.photoId as profilePhotoId,
                           pl.channel.profile.photo.path as profilePhotoUrl
                         from PlayListEntity as pl
                         where pl.channelId = ?1
            """)
    Page<PlayListInfo> findAllPagination(String channelId, Pageable pageable);

    @Query("""
                    select pl.id as id,
                           pl.name as name,
                           pl.description as description,
                           pl.status as status,
                           pl.orderNumber as orderNumber,
                           pl.channelId as channelId,
                           pl.channel.name as channelName,
                           pl.channel.photoId as channelPhotoId,
                           pl.channel.photo.path as channelPhotoUrl,
                           pl.channel.profileId as profileId,
                           pl.channel.profile.name as profileName,
                           pl.channel.profile.surname as profileSurname,
                           pl.channel.profile.photoId as profilePhotoId,
                           pl.channel.profile.photo.path as profilePhotoUrl
                         from PlayListEntity as pl
                         where pl.channel.profileId = ?1
                         order by pl.orderNumber desc
            """)
    List<PlayListInfo> findByUserId(String userId);

    @Query("""
                    select pl.id as id,
                           pl.name as name,
                           pl.channelId as channelId,
                           pl.channel.name as channelName
                         from PlayListEntity as pl
                         where pl.channel.profileId = ?1
                         order by pl.orderNumber desc
            """)
    List<PlayListInfo> findAllByUserId(String userId);
}
