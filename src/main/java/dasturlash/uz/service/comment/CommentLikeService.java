package dasturlash.uz.service.comment;

import dasturlash.uz.dto.comment.CommentLikeInfo;
import dasturlash.uz.entity.comment.CommentLikeEntity;
import dasturlash.uz.enums.Emotion;
import dasturlash.uz.repository.comment.CommentLikeRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public void merge(String commentId, Emotion emotion){
        String profileId = SpringSecurityUtil.currentProfileId();
        Optional<CommentLikeEntity> commentLikeCheck = commentLikeRepository
                .findCommentLikeEntityByCommentIdAndProfileId(commentId, profileId);
        if (commentLikeCheck.isPresent()){
            CommentLikeEntity commentLike = commentLikeCheck.get();
            if (commentLike.getEmotion().equals(emotion)){
                commentLikeRepository.delete(commentLike);
            } else {
                commentLike.setEmotion(emotion);
                commentLikeRepository.save(commentLike);
            }
        } else {
            CommentLikeEntity commentLike = new CommentLikeEntity();
            commentLike.setCommentId(commentId);
            commentLike.setProfileId(profileId);
            commentLike.setEmotion(emotion);
            commentLikeRepository.save(commentLike);
        }
    }

    public List<CommentLikeInfo> getCommentsLikedUser(){
        String profileId = SpringSecurityUtil.currentProfileId();
        return commentLikeRepository.getCommentsLikedUser(profileId);
    }
}
