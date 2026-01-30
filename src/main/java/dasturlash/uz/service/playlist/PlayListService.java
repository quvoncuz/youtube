package dasturlash.uz.service.playlist;

import dasturlash.uz.dto.playlist.PlayListDTO;
import dasturlash.uz.dto.playlist.PlayListInfo;
import dasturlash.uz.entity.playlist.PlayListEntity;
import dasturlash.uz.enums.PlayListStatus;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.playlist.PlayListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayListService {

    @Autowired
    private PlayListRepository playListRepository;

    public PlayListDTO create(PlayListDTO dto){
        PlayListEntity playList = new PlayListEntity();
        playList.setName(dto.getName());
        playList.setDescription(dto.getDescription());
        playList.setStatus(dto.getStatus());
        playList.setChannelId(dto.getChannel().getId());
        playList.setOrderNumber(dto.getOrderNumber());
        playList = playListRepository.save(playList);
        dto.setId(playList.getId());

        return dto;
    }

    public PlayListDTO update(PlayListDTO dto, String playListId){
        PlayListEntity playList = playListRepository.findById(playListId).orElseThrow(() -> new AppBadException("Not found"));
        playList.setName(dto.getName());
        playList.setDescription(dto.getDescription());
        playList.setStatus(dto.getStatus());
        playList.setOrderNumber(dto.getOrderNumber());
        playList = playListRepository.save(playList);
        dto.setId(playList.getId());

        return dto;
    }

    public boolean updateStatus(PlayListStatus status, String playListId){
        return playListRepository.updateStatus(status.toString(), playListId) == 1;
    }

    public void deletePlayList(String id){
        playListRepository.deleteById(id);
    }

    public Page<PlayListInfo> pagination(String channelId, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return playListRepository.findAllPagination(channelId, pageRequest);
    }

    public List<PlayListInfo> getAllByUserId(String userId){
        return playListRepository.findByUserId(userId);
    }


}
