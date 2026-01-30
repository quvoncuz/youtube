package dasturlash.uz.service.email;

import dasturlash.uz.dto.email.EmailDTO;
import dasturlash.uz.dto.email.EmailFilterRequestDTO;
import dasturlash.uz.dto.email.FilterResult;
import dasturlash.uz.entity.EmailHistoryEntity;
import dasturlash.uz.repository.email.CustomFilterRepository;
import dasturlash.uz.repository.email.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    @Autowired
    private CustomFilterRepository customFilterRepository;

    public void create(EmailDTO dto) {
        EmailHistoryEntity email = new EmailHistoryEntity();
        email.setCode(dto.getSmsCode());
        email.setToEmail(dto.getToAccount());
        email.setMessage(dto.getBody());
        email.setTitle("Confirmation mail");

        emailHistoryRepository.save(email);
    }

    public EmailHistoryEntity getLastByEmail(String email) {
        return emailHistoryRepository.findByToEmailOrderByCreatedDateDesc(email).orElseThrow(() -> new RuntimeException(""));
    }

    public PageImpl<EmailDTO> getPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmailHistoryEntity> allByPagination = emailHistoryRepository.findAll(pageRequest);
        List<EmailDTO> resultList = allByPagination
                .stream().map(this::toDTO).toList();
        return new PageImpl<>(resultList, pageRequest, allByPagination.getTotalElements());
    }

    public PageImpl<EmailDTO> getEmailHistoryByEmail(String email, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmailHistoryEntity> allByPagination = emailHistoryRepository.findAllByToEmail(email, pageRequest);
        List<EmailDTO> resultList = allByPagination
                .stream().map(this::toDTO).toList();
        return new PageImpl<>(resultList, pageRequest, allByPagination.getTotalElements());
    }

    public PageImpl<EmailDTO> getAllByFilter(EmailFilterRequestDTO dto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        FilterResult<Object[]> filterResult = customFilterRepository.filterEmailHistory(dto, page, size);

        List<EmailDTO> resultList = filterResult.getContent()
                .stream().map(object -> {
                    EmailDTO emailDTO = new EmailDTO();
                    emailDTO.setToAccount((String) object[1]);
                    emailDTO.setSmsCode((String) object[2]);
                    emailDTO.setBody((String) object[3]);
                    return emailDTO;
                }).toList();

        return new PageImpl<>(resultList, pageRequest, filterResult.getTotalElement());
    }

    private EmailDTO toDTO(EmailHistoryEntity entity) {
        EmailDTO dto = new EmailDTO();
        dto.setToAccount(entity.getToEmail());
        dto.setBody(entity.getMessage());
        dto.setSmsCode(entity.getCode());
        return dto;
    }
}
