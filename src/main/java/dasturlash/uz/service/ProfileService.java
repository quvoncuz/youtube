package dasturlash.uz.service;

import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.entity.AttachEntity;
import dasturlash.uz.entity.EmailHistoryEntity;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.Lang;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.exception.UserException;
import dasturlash.uz.repository.AttachRepository;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.service.email.EmailSendingService;
import dasturlash.uz.service.email.EmailService;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AttachRepository attachRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AttachService attachService;

    public String changePassword(String oldPassword, String newPassword, Lang lang) {
        ProfileEntity profile = profileRepository
                .findById(SpringSecurityUtil.currentProfileId())
                .orElseThrow(() -> new UserException(messageSource.getMessage("user.not.found", null, new Locale(lang.name()))));
        if (!bCryptPasswordEncoder.matches(oldPassword, profile.getPassword())) {
            throw new UserException(messageSource.getMessage("wrong.password", null, new Locale(lang.name())));
        }
        profile.setPassword(bCryptPasswordEncoder.encode(newPassword));
        profileRepository.save(profile);
        return "Successfully changed";
    }

    public String updateEmail(String newEmail, String code, Lang lang) {
        if (code == null) {
            ProfileEntity profile = profileRepository.findById(SpringSecurityUtil.currentProfileId())
                    .orElseThrow(() -> new UserException("User not found"));
            Optional<ProfileEntity> byEmail = profileRepository.findByEmail(newEmail);
            if (byEmail.isPresent()) {
                throw new UserException(messageSource.getMessage("user.already.exist", null, new Locale(lang.name())));
            }
            emailSendingService.sendRegistrationStyledEmail(profile.getName(), newEmail);
            return "Confirmation code send";
        } else {
            ProfileEntity profile = profileRepository.findById(SpringSecurityUtil.currentProfileId())
                    .orElseThrow(() -> new UserException("User not found"));
            EmailHistoryEntity lastByEmail = emailService.getLastByEmail(newEmail);
            if (lastByEmail.getCreatedDate().plusMinutes(5).isBefore(LocalDateTime.now())) {
                throw new AppBadException("Code expired");
            }
            if (!lastByEmail.getCode().equals(code)) {
                throw new AppBadException("Wrong code!");
            }
            profile.setEmail(newEmail);
            profileRepository.save(profile);
            return "Changed";
        }
    }

    public ProfileDTO updateDetails(ProfileDTO dto, Lang lang) {
        ProfileEntity profile = profileRepository.findById(SpringSecurityUtil.currentProfileId())
                .orElseThrow(() -> new UserException(messageSource.getMessage("user.not.found", null, new Locale(lang.name()))));
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        dto.setId(profile.getId());
        return dto;
    }

    public void updateAttach(MultipartFile file, Lang lang) {
        ProfileEntity profile = profileRepository.findById(SpringSecurityUtil.currentProfileId())
                .orElseThrow(() -> new UserException(messageSource.getMessage("user.not.found", null, new Locale(lang.name()))));
        Optional<AttachEntity> attach = attachRepository.findById(profile.getPhotoId());
        if (attach.isPresent()) {
            attachService.delete(attach.get().getId());
        }
        AttachDTO upload = attachService.upload(file);
        profile.setPhotoId(upload.getId());
    }

    public ProfileDTO createByAdmin(ProfileDTO dto, Lang lang) {
        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent()) {
            throw new UserException(messageSource.getMessage("user.already.exist", null, new Locale(lang.name())));
        }

        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setRoles(dto.getRole());
        profile.setEmail(dto.getEmail());
        profile.setStatus(Status.ACTIVE);
        profile = profileRepository.save(profile);
        dto.setId(profile.getId());
        return dto;
    }
}
