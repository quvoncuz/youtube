package dasturlash.uz.service;

import dasturlash.uz.dto.TagDTO;
import dasturlash.uz.entity.TagEntity;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagDTO create(TagDTO dto){
        Optional<TagEntity> tagEntity = tagRepository.findTagEntityByName(dto.getName());
        if (tagEntity.isPresent()){
            throw new AppBadException("Already exists");
        }
        TagEntity tag = new TagEntity();
        tag.setName(dto.getName());
        tag = tagRepository.save(tag);
        dto.setId(tag.getId());
        return dto;
    }

    public TagDTO update(TagDTO dto, Integer id){
        TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new AppBadException("Not found"));
        Optional<TagEntity> tagEntityByName = tagRepository.findTagEntityByName(dto.getName());
        if (tagEntityByName.isPresent()){
            throw new AppBadException("Already exists");
        }
        tag.setName(dto.getName());
        tag = tagRepository.save(tag);
        dto.setId(tag.getId());
        return dto;
    }

    public void delete(Integer id){
        tagRepository.deleteById(id);
    }

    public List<TagDTO> getAll(){
        List<TagEntity> all = tagRepository.findAll();
        List<TagDTO> resultList = new ArrayList<>();
        all.forEach(tag -> resultList.add(toDTO(tag)));
        return resultList;
    }

    private TagDTO toDTO(TagEntity tag){
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
