package dasturlash.uz.service.video;

import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.dto.ChannelDTO;
import dasturlash.uz.dto.TagDTO;
import dasturlash.uz.dto.video.VideoDTO;
import dasturlash.uz.dto.video.VideoFullInfo;
import dasturlash.uz.dto.video.VideoShortInfo;
import dasturlash.uz.entity.video.VideoEntity;
import dasturlash.uz.enums.Lang;
import dasturlash.uz.enums.StatusEnum;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.video.VideoRepository;
import dasturlash.uz.service.AttachService;
import dasturlash.uz.service.ChannelService;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Locale;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoWatchedService videoWatchedService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ObjectMapper objectMapper;

    public VideoDTO create(VideoDTO dto, MultipartFile file, String channerId){
        VideoEntity video = new VideoEntity();
        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setStatus(dto.getStatus());
        video.setChannelId(dto.getChannelDTO().getId());
        video.setCategoryId(dto.getCategory().getId());
        video.setViewCount(0L);
        video.setSharedCount(0L);
        video.setPreviewAttachId(dto.getPreviewVideoId());

        AttachDTO videoAttach = attachService.upload(file);
        video.setAttachId(videoAttach.getId());

        video = videoRepository.save(video);
        dto.setId(video.getId());
        dto.setCreatedDate(video.getCreatedDate());
        dto.setAttachDTO(videoAttach);

        return dto;
    }

    public VideoDTO update(VideoDTO dto, String id, Lang lang){
        VideoEntity video = videoRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageSource.getMessage("video.not.found", null, new Locale(lang.name()))));
        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setCategoryId(dto.getCategory().getId());

        video = videoRepository.save(video);

        dto.setId(video.getId());

        return dto;
    }

    public boolean updateStatus(StatusEnum status, String id){
        return videoRepository.updateStatus(status, id) == 1;
    }

    public Long increaseViewCount(String videoId){
        videoWatchedService.create(SpringSecurityUtil.currentProfileId(), videoId);
        Long count = videoWatchedService.findVideoCount(videoId);
        videoRepository.updateVideoCount(videoId, count);
        return count;
    }

    public Page<VideoShortInfo> getVideoPaginationByCategoryId(Integer categoryId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return videoRepository.getVideoEntitiesByCategoryId(categoryId, pageRequest);
    }

    public Page<VideoShortInfo> searchVideosByTitle(String title, int size){
        PageRequest pageRequest = PageRequest.of(0, size);
        return videoRepository.searchVideosByTitle(title.toLowerCase(), pageRequest);
    }

    public Page<VideoShortInfo> getVideosByTagId(String tagId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return videoRepository.getVideosByTagId(tagId, pageRequest);
    }

    public VideoFullInfo getVideoById(String videoId, Lang lang){
        VideoFullInfo videoFullInfo = videoRepository.getVideoFullInfo(videoId, SpringSecurityUtil.currentProfileId());

        List<TagDTO> tags = objectMapper.readValue(
                videoFullInfo.getTagList(),
                new TypeReference<List<TagDTO>>() {}
        );

        if (videoFullInfo.getStatus().equals(StatusEnum.PRIVATE)){
            ChannelDTO channelById = channelService.getChannelById(videoFullInfo.getChannelId());
            if (channelById.getProfile().getId().equals(SpringSecurityUtil.currentProfileId())){
                return videoFullInfo;
            } else {
                throw new AppBadException(messageSource.getMessage("video.not.found", null, new Locale(lang.name())));
            }
        }
        return videoFullInfo;
    }



    private VideoDTO toDTO(VideoEntity video){
        VideoDTO dto = new VideoDTO();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setStatus(video.getStatus());
        dto.setViewCount(video.getViewCount());
        dto.setSharedCount(video.getSharedCount());
        dto.setCreatedDate(video.getCreatedDate());
        // Additional mappings can be added here
        return dto;
    }

}
