package dasturlash.uz.service.playlist;

import dasturlash.uz.dto.playlist.PlayListVideoInfo;
import dasturlash.uz.entity.playlist.PlayListVideoEntity;
import dasturlash.uz.enums.Lang;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.playlist.PlayListVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PlayListVideoService {

    @Autowired
    private PlayListVideoRepository playListVideoRepository;
    @Autowired
    private MessageSource messageSource;

    public void createRelation(String playListId, String videoId, int orderNumber, Lang lang) {
        Optional<PlayListVideoEntity> playListVideo = playListVideoRepository.findPlayListVideoEntityByPlayListIdAndVideoId(playListId, videoId);
        if (playListVideo.isPresent()) {
            throw new AppBadException(messageSource.getMessage("already.exist", null, new Locale(lang.name())));
        }
        PlayListVideoEntity entity = new PlayListVideoEntity();
        entity.setPlayListId(playListId);
        entity.setVideoId(videoId);
        entity.setOrderNumber(orderNumber);
        playListVideoRepository.save(entity);
    }

    public void updateRelation(String playListId, String videoId, int orderNumber, Lang lang) {
        PlayListVideoEntity playListVideo = playListVideoRepository.findPlayListVideoEntityByVideoId(videoId)
                .orElseThrow(() -> new AppBadException(messageSource.getMessage("not.found", null, new Locale(lang.name()))));
        playListVideo.setPlayListId(playListId);
        playListVideo.setOrderNumber(orderNumber);
        playListVideoRepository.save(playListVideo);
    }

    public void deleteRelation(String playListId, String videoId, Lang lang) {
        playListVideoRepository.deletePlayListVideoEntityByPlayListIdAndVideoId(playListId, videoId);
    }

    public List<PlayListVideoInfo> getAllByPlayListId(String playListId) {
        return playListVideoRepository.findAllByPlayListId(playListId);
    }
}