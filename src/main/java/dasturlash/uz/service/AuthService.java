package dasturlash.uz.service;

import dasturlash.uz.dto.RegistrationDTO;
import dasturlash.uz.entity.EmailHistoryEntity;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.Lang;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.exception.UserException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.service.email.EmailSendingService;
import dasturlash.uz.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AttachService attachService;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegistrationDTO registration(RegistrationDTO dto, MultipartFile file, Lang lang){
        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent()){
            throw new UserException(messageSource.getMessage("User.already.exists", null, new Locale(lang.name())));
        }
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setEmail(dto.getEmail());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setPhotoId(attachService.upload(file).getId());
        profile.setRoles(Role.ROLE_USER);
        profile.setStatus(Status.ACTIVE);
        profile = profileRepository.save(profile);
        emailSendingService.sendRegistrationStyledEmail(dto.getName(), dto.getEmail());
        dto.setId(profile.getId());
        return dto;
    }

    public boolean confirmation(String sms, String email, Lang lang){
        ProfileEntity profile = profileRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found"));

        if (!profile.getStatus().equals(Status.NOT_ACTIVE)){
            throw new UserException("User on wrong status");
        }

        EmailHistoryEntity emailHistory = emailService.getLastByEmail(email);
        if (emailHistory.getCreatedDate().plusMinutes(5).isBefore(LocalDateTime.now())){
            throw new AppBadException(messageSource.getMessage("code.expired", null, new Locale(lang.name())));
        }
        if (!emailHistory.getCode().equals(sms)){
            throw new AppBadException(messageSource.getMessage("wrong.code", null, new Locale(lang.name())));
        }
        profile.setStatus(Status.ACTIVE);
        profileRepository.save(profile);
        return true;
    }

    public boolean login(){
        return false;
    }
}
