package dasturlash.uz.service;

import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.entity.AttachEntity;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;

    private final String mainFolder = "attaches";

    public AttachDTO upload(MultipartFile multipartFile) {
        try {
            File folder = new File(mainFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String extension = getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String fileName = UUID.randomUUID().toString();
            String folderPath = getMkdir();
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(mainFolder + "/" + folderPath + "/" + fileName + "." + extension);
            Files.write(path, bytes);

            AttachEntity attach = new AttachEntity();
            attach.setId(fileName);
            attach.setExtension(extension);
            attach.setSize(multipartFile.getSize());
            attach.setPath(mainFolder + "/" + folderPath);
            attach.setOriginName(multipartFile.getOriginalFilename());
            attachRepository.save(attach);
            return toDTO(attach);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> open(String attachId) {
        AttachEntity attach = attachRepository.findById(attachId)
                .orElseThrow(() -> new AppBadException("Not found"));
        Path filePath = Paths.get(getPath(attach)).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new AppBadException("File not found");
            }
            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<?> download(String attachId) {
        try {
            AttachEntity attach = attachRepository.findById(attachId)
                    .orElseThrow(() -> new AppBadException("Not found"));
            Path filePath = Paths.get(getPath(attach)).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!(resource.exists() || resource.isReadable())) {
                throw new AppBadException("File not found");
            }

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attach.getOriginName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public boolean delete(String attachId) {
        AttachEntity attach = attachRepository.findById(attachId)
                .orElseThrow(() -> new AppBadException("Not found"));
        String path = attach.getPath() + "/" + attach.getId() + "." + attach.getExtension();
        File file = new File(path);
        attachRepository.delete(attach);
        return file.delete();
    }

    public PageImpl<AttachDTO> paginationByAdmin(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AttachEntity> entityPage = attachRepository.findAll(pageRequest);
        List<AttachDTO> resultList = new LinkedList<>();
        entityPage.forEach(attach -> resultList.add(toDTO(attach)));
        return new PageImpl<>(resultList, pageRequest, entityPage.getTotalElements());
    }

    private AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setSize(entity.getSize());
        dto.setPath(entity.getPath());
        dto.setOriginName(entity.getOriginName());
        dto.setExtension(entity.getExtension());

        return dto;
    }

    private String getPath(AttachEntity entity) {
        return entity.getPath() + "/" + entity.getId();
    }

    private String getMkdir() {
        int year = LocalDateTime.now().getYear();
        int month = LocalDate.now().getMonthValue() + 1;
        int day = LocalDate.now().getDayOfMonth();
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        return year + "/" + month + "/" + day + "/" + hour + "/" + minute;
    }

    private String getExtension(String file) {
        return file.substring(file.lastIndexOf(".") + 1);
    }
}
