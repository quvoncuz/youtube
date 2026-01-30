package dasturlash.uz.service;

import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.dto.ChannelDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.entity.ChannelEntity;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.exception.UserException;
import dasturlash.uz.repository.ChannelRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private AttachService attachService;

    public ChannelDTO create(ChannelDTO dto, MultipartFile photo, MultipartFile banner) {
        Optional<ChannelEntity> channelEntityByUsername = channelRepository.findChannelEntityByUsername(dto.getUsername());
        if (channelEntityByUsername.isPresent()) {
            throw new AppBadException("User already exists");
        }
        ChannelEntity channel = new ChannelEntity();
        channel.setName(dto.getName());
        channel.setUsername(dto.getUsername());
        channel.setDescription(dto.getDescription());
        channel.setPhotoId(SpringSecurityUtil.currentProfileId());
        AttachDTO attachPhoto = attachService.upload(photo);
        channel.setPhotoId(attachPhoto.getId());
        AttachDTO attachBanner = attachService.upload(banner);
        channel.setBannerId(attachBanner.getId());
        channel.setStatus(Status.ACTIVE);
        channel = channelRepository.save(channel);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(SpringSecurityUtil.currentProfileId());
        dto.setId(channel.getId());
        dto.setImage(attachPhoto);
        dto.setBanner(attachBanner);
        dto.setProfile(profileDTO);

        return dto;
    }

    public ChannelDTO update(ChannelDTO dto, String id) {
        ChannelEntity channel = channelRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Not found"));
        if (!channel.getPhotoId().equals(SpringSecurityUtil.currentProfileId())){
            throw new UserException("You are not owner");
        }
        Optional<ChannelEntity> channelEntityByUsername = channelRepository.findChannelEntityByUsername(dto.getUsername());
        if (channelEntityByUsername.isPresent()) {
            if (!channel.getId().equals(channelEntityByUsername.get().getId())) {
                throw new AppBadException("User not available");
            } else {
                channel.setUsername(dto.getUsername());
            }
        }
        channel.setName(dto.getName());
        channel.setDescription(dto.getDescription());
        channel = channelRepository.save(channel);
        dto.setId(channel.getId());
        return dto;
    }

    public AttachDTO updatePhoto(MultipartFile photo, String channelId) {
        ChannelEntity channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppBadException("Channel not found"));
        if (!channel.getPhotoId().equals(SpringSecurityUtil.currentProfileId())){
            throw new UserException("You are not owner");
        }
        attachService.delete(channel.getPhotoId());
        AttachDTO attachPhoto = attachService.upload(photo);
        channel.setPhotoId(attachPhoto.getId());
        return attachPhoto;
    }

    public AttachDTO updateBanner(MultipartFile banner, String channelId){
        ChannelEntity channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppBadException("Channel not found"));
        if (!channel.getPhotoId().equals(SpringSecurityUtil.currentProfileId())){
            throw new UserException("You are not owner");
        }
        attachService.delete(channel.getBannerId());
        AttachDTO attachBanner = attachService.upload(banner);
        channel.setBannerId(attachBanner.getId());
        return attachBanner;
    }

    public PageImpl<ChannelDTO> pagination(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ChannelEntity> all = channelRepository.findAll(pageRequest);
        List<ChannelDTO> resultList = new ArrayList<>();
        all.forEach(entity -> resultList.add(toDTO(entity)));
        return new PageImpl<>(resultList, pageRequest, all.getTotalElements());
    }

    public ChannelDTO getChannelById(String channelId){
        ChannelEntity channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppBadException("Channel not found"));
        return toDTO(channel);
    }

    public boolean changeChannelStatus(Status status, String channelId){
        return channelRepository.updateStatus(status.toString(), channelId) == 1;
    }

    public List<ChannelDTO> getOwnChannelList(){
        String profileId = SpringSecurityUtil.currentProfileId();
        List<ChannelEntity> channelEntitiesByProfileId = channelRepository.findChannelEntitiesByProfileId(profileId);
        List<ChannelDTO> resultList = new ArrayList<>();
        channelEntitiesByProfileId.forEach(entity -> resultList.add(toDTO(entity)));

        return resultList;
    }

    private ChannelDTO toDTO(ChannelEntity entity){
        ChannelDTO dto = new ChannelDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setUsername(entity.getUsername());

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(entity.getProfileId());
        dto.setProfile(profileDTO);

        AttachDTO photo = new AttachDTO();
        photo.setId(entity.getPhotoId());
        dto.setImage(photo);

        AttachDTO banner = new AttachDTO();
        banner.setId(entity.getBannerId());
        dto.setBanner(banner);

        return dto;
    }
}
