package dasturlash.uz.service;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.entity.CategoryEntity;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public  CategoryDTO create(CategoryDTO dto){
        Optional<CategoryEntity> categoryEntityByName = categoryRepository.findCategoryEntityByName(dto.getName());
        if (categoryEntityByName.isPresent()){
            throw new AppBadException("Already exists");
        }
        CategoryEntity category = new CategoryEntity();
        category.setName(dto.getName());
        category = categoryRepository.save(category);
        dto.setId(category.getId());
        return dto;
    }

    public CategoryDTO update(CategoryDTO dto, Integer id){
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new AppBadException("Not found"));
        Optional<CategoryEntity> categoryEntityByName = categoryRepository.findCategoryEntityByName(dto.getName());
        if (categoryEntityByName.isPresent()){
            throw new AppBadException("Already exists");
        }
        category.setName(dto.getName());
        category = categoryRepository.save(category);
        dto.setId(category.getId());
        return dto;
    }

    public void delete(Integer categoryId){
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryDTO> getCategories(){
        List<CategoryEntity> entities = categoryRepository.findAll();
        List<CategoryDTO> resultList = new ArrayList<>();
        entities.forEach(entity -> resultList.add(toDTO(entity)));
        return resultList;
    }

    private CategoryDTO toDTO(CategoryEntity entity){
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
