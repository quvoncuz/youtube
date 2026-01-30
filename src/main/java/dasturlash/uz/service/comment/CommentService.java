package dasturlash.uz.service.comment;

import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.comment.CommentInfo;
import dasturlash.uz.dto.comment.CommentMapper;
import dasturlash.uz.dto.video.VideoDTO;
import dasturlash.uz.entity.comment.CommentEntity;
import dasturlash.uz.repository.comment.CommentRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentDTO create(CommentDTO dto){
        CommentEntity comment = new CommentEntity();
        comment.setContent(dto.getContent());
        comment.setVideoId(dto.getVideo().getId());
        comment.setProfileId(SpringSecurityUtil.currentProfileId());
        comment.setReplyId(dto.getReplyId());

        comment = commentRepository.save(comment);
        dto.setId(comment.getId());

        return dto;
    }

    public void update(String content, String commentId){
        commentRepository.updateContent(content, commentId);
    }

    public void delete(String commentId){
        commentRepository.deleteById(commentId);
    }

    public PageImpl<CommentDTO> commentPagination(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CommentEntity> allForPagination = commentRepository.findAll(pageRequest);
        List<CommentDTO> resultList = allForPagination
                .stream().map(this::toDTO).toList();

        return new PageImpl<>(resultList, pageRequest, allForPagination.getTotalElements());
    }

    public Page<CommentMapper> commentsByProfileId(String profileId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findCommentEntities(profileId, pageRequest);
    }

    public Page<CommentMapper> getCurrentProfileComments(int page, int size){
        String profileId = SpringSecurityUtil.currentProfileId();
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findCommentEntities(profileId, pageRequest);
    }

    public Page<CommentInfo> getCommentsByVideoId(String videoId){
        PageRequest pageRequest = PageRequest.of(0, 20);
        return commentRepository.getCommentInfoByVideoId(videoId, pageRequest);
    }

    public List<CommentInfo> getCommentsByReplyId(String replyId){
        return commentRepository.getCommentInfoByReplyId(replyId);
    }

    private CommentDTO toDTO(CommentMapper mapper){
        CommentDTO dto = new CommentDTO();
        dto.setId(mapper.getId());
        dto.setContent(mapper.getContent());

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(mapper.getVideoId());
        videoDTO.setTitle(mapper.getTitle());
        videoDTO.setPreviewVideoId(mapper.getPreviewVideoId());
        dto.setVideo(videoDTO);

        dto.setLikeCount(mapper.getLikeCount());
        dto.setDislikeCount(mapper.getDislikeCount());

        return dto;
    }
    private CommentDTO toDTO(CommentEntity entity){
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setProfileId(entity.getProfileId());
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(entity.getVideoId());
        dto.setVideo(videoDTO);


        return dto;
    }
}
