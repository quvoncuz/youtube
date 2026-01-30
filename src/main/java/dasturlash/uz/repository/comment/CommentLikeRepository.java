package dasturlash.uz.repository.comment;

import dasturlash.uz.dto.comment.CommentLikeInfo;
import dasturlash.uz.entity.comment.CommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, String> {
    Optional<CommentLikeEntity> findCommentLikeEntityByCommentIdAndProfileId(String commentId, String profileId);

    @Query("""
            select cl.id as id,
                   cl.profileId as profileId,
                   cl.commentId as commentId,
                   cl.createdDate as createdDate,
                   case when cl.emotion = 'LIKE' then 'LIKE' else 'DISLIKE' end as type
            from CommentLikeEntity as cl
            where cl.profileId = ?1
            order by cl.createdDate desc
            """)
    List<CommentLikeInfo> getCommentsLikedUser(String profileId);

}
