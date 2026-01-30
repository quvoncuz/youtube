package dasturlash.uz.service.video;

import dasturlash.uz.dto.video.VideoLikeInfo;
import dasturlash.uz.entity.video.VideoLikeEntity;
import dasturlash.uz.enums.Emotion;
import dasturlash.uz.repository.video.VideoLikeRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoLikeService {

    @Autowired
    private VideoLikeRepository videoLikeRepository;

    public void merge(String videoId, Emotion emotion){
        String profileId = SpringSecurityUtil.currentProfileId();
        Optional<VideoLikeEntity> videoLikeCheck = videoLikeRepository
                .findVideoLikeEntityByVideoIdAndProfileId(videoId, profileId);
        if (videoLikeCheck.isPresent()){
            VideoLikeEntity videoLike = videoLikeCheck.get();
            if (videoLike.getEmotion().equals(emotion)){
                videoLikeRepository.delete(videoLike);
            } else {
                videoLike.setEmotion(emotion);
                videoLikeRepository.save(videoLike);
            }
        } else {
            VideoLikeEntity videoLike = new VideoLikeEntity();
            videoLike.setVideoId(videoId);
            videoLike.setProfileId(profileId);
            videoLike.setEmotion(emotion);
            videoLikeRepository.save(videoLike);
        }
    }

    public List<VideoLikeInfo> getVideoLikedByProfileId(){
        String profileId = SpringSecurityUtil.currentProfileId();
        return videoLikeRepository.getVideoLikedByProfileId(profileId);
    }

    public List<VideoLikeInfo> getVideoLikedByProfileIdByAdmin(String profileId){
        String adminId = SpringSecurityUtil.currentProfileId();
        return videoLikeRepository.getVideoLikedByProfileId(profileId, adminId);
    }
}
