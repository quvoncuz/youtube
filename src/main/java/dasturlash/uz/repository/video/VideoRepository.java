package dasturlash.uz.repository.video;

import dasturlash.uz.dto.video.VideoFullInfo;
import dasturlash.uz.dto.video.VideoShortInfo;
import dasturlash.uz.entity.video.VideoEntity;
import dasturlash.uz.enums.StatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, String> {

    @Modifying
    @Transactional
    @Query("update VideoEntity set status = ?1 where id = ?2")
    int updateStatus(StatusEnum status, String id);

    @Modifying
    @Transactional
    @Query("update VideoEntity set viewCount = ?2 where id = ?1")
    void updateVideoCount(String videoId, Long count);

    @Query("""
            select v.id ,
                   v.title,
                   v.previewAttachId,
                   v.previewAttach.path
                from VideoEntity as v
                where v.categoryId = ?1
            """)
    Page<VideoShortInfo> getVideoEntitiesByCategoryId(Integer categoryId, Pageable pageable);

    @Query("""
            select v.id ,
                   v.title,
                   v.previewAttachId,
                   v.previewAttach.path
                from VideoEntity as v
                where lower(v.title) like %?1%
            """)
    Page<VideoShortInfo> searchVideosByTitle(String title, Pageable pageable);

    @Query("""
            select v.id ,
                   v.title,
                   v.previewAttachId,
                   v.previewAttach.path
                from VideoEntity as v
                where v.id in (
                    select tv.videoId
                    from VideoTagEntity as tv
                    where tv.tagId = ?1
                )
            """)
    Page<VideoShortInfo> getVideosByTagId(String tagId, Pageable pageable);

    @Query(value = """
            SELECT 
                v.id as id,
                v.title as title,
                v.description as description,
                v.status as status,

                pa.id as previewAttachId,
                pa.url as previewAttachUrl,

                a.id as attachId,
                a.url as attachUrl,
                a.duration as attachDuration,

                c.id as categoryId,
                c.name as categoryName,

                COALESCE(
                    json_agg(
                        json_build_object(
                            'id', t.id,
                            'name', t.name
                        )
                    ) FILTER (WHERE t.id IS NOT NULL),
                    '[]'
                ) as tagList,

                v.published_date as publishedDate,
            
                ch.id as channelId,
                ch.name as channelName,
                ch.photo_url as channelPhotoUrl,

                v.view_count as viewCount,
                v.shared_count as sharedCount,
                v.like_count as likeCount,
                v.dislike_count as dislikeCount,

                CASE
                    WHEN vl.emotion = 'LIKE' THEN true
                    ELSE false
                END as isUserLiked,
                
                CASE
                    WHEN vl.emotion = 'DISLIKE' THEN true
                    ELSE false
                END as isUserDisliked

            FROM video v
            LEFT JOIN category c ON c.id = v.category_id
            LEFT JOIN attach pa ON pa.id = v.preview_attach_id
            LEFT JOIN attach a ON a.id = v.attach_id
            LEFT JOIN channel ch ON ch.id = v.channel_id

            LEFT JOIN video_tags vt ON vt.video_id = v.id
            LEFT JOIN tag t ON t.id = vt.tag_id
            LEFT JOIN video_likes vl\s
                ON vl.video_id = v.id\s
               AND vl.profile_id = ?2

            WHERE v.id = ?1

            GROUP BY 
                v.id, pa.id, a.id, c.id, ch.id, vl.is_liked, vl.is_disliked
            """, nativeQuery = true)
    VideoFullInfo getVideoFullInfo(String videoId,
                                   String profileId);

}
