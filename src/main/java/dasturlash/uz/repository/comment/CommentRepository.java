package dasturlash.uz.repository.comment;

import dasturlash.uz.dto.comment.CommentInfo;
import dasturlash.uz.dto.comment.CommentMapper;
import dasturlash.uz.entity.comment.CommentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    @Modifying
    @Transactional
    @Query("""
        update CommentEntity set content = ?1 where id = ?2
        """)
    void updateContent(String content, String commentId);

    @Query("""
            select c.id as id,
                   c.content as content,
                   c.videoId as videoId,
                   c.video.title as videoTitle,
                   c.video.previewAttachId as previewVideoId,
                   c.createdDate as createdDate,
                   count (case when cl.emotion = 'LIKE' then 1
                               else  0
                               end ) as likeCount,
                   count (case when cl.emotion = 'DISLIKE' then 1
                               else  0
                               end ) as dislikeCount
            from CommentEntity as c
            left join CommentLikeEntity as cl on cl.commentId = c.id
            where c.profileId = ?1
            """)
    Page<CommentMapper> findCommentEntities(String profileId, Pageable pageable);

    @Query("""
    select 
        c.id as id,
        c.content as content,
        c.createdDate as createdDate,

        count(case when cl.emotion = 'LIKE' then 1 else 0 end) as likeCount,
        count(case when cl.emotion = 'DISLIKE' then 1 else 0 end) as dislikeCount,

        p.id as profileId,
        p.name as profileName,
        p.surname as profileSurname,
        p.photoId as profilePhotoId,
        a.path as profilePhotoUrl

    from CommentEntity c

    left join CommentLikeEntity cl 
        on cl.commentId = c.id

    left join ProfileEntity p 
        on p.id = c.profileId

    left join AttachEntity a
        on a.id = p.photoId

    where c.videoId = ?1

    group by 
        c.id, c.content, c.createdDate,
        p.id, p.name, p.surname, p.photoId, a.path
""")
    Page<CommentInfo> getCommentInfoByVideoId(String videoId, Pageable pageable);

    @Query("""
        select c from CommentEntity as c
        where c.replyId = ?1
        """)
    List<CommentInfo> getCommentInfoByReplyId(String replyId);
}
