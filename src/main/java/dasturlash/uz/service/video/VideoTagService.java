package dasturlash.uz.service.video;

import dasturlash.uz.dto.TagDTO;
import dasturlash.uz.dto.video.VideoTagDTO;
import dasturlash.uz.dto.video.VideoTagMapper;
import dasturlash.uz.entity.video.VideoTagEntity;
import dasturlash.uz.enums.Lang;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.video.VideoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class VideoTagService {

    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private MessageSource messageSource;

    public void createRelation(String videoId, Integer tagId, Lang lang) {
        Optional<VideoTagEntity> videoTag = videoTagRepository.findVideoTagEntityByVideoIdAndTagId(videoId, tagId);
        if (videoTag.isPresent()) {
            throw new AppBadException(messageSource.getMessage("already.exist", null, new Locale(lang.name())));
        }
        VideoTagEntity videoTagEntity = new VideoTagEntity();
        videoTagEntity.setVideoId(videoId);
        videoTagEntity.setTagId(tagId);
        videoTagRepository.save(videoTagEntity);
    }

    public void deleteRelation(String videoId, Integer tagId) {
        videoTagRepository.deleteVideoTagEntityByVideoIdAndTagId(videoId, tagId);
    }

    public List<VideoTagDTO> getTagListByVideoId(String videoId) {
        List<VideoTagMapper> allTagByVideoId = videoTagRepository.findAllByVideoId(videoId);
        return allTagByVideoId.stream().map(this::toDTO).toList();
    }

    private VideoTagDTO toDTO(VideoTagMapper mapper) {
        VideoTagDTO dto = new VideoTagDTO();
        dto.setVideoId(mapper.getVideoId());
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(mapper.getTagId());
        tagDTO.setName(mapper.getTagName());

        dto.setTag(tagDTO);
        return dto;
    }


}
