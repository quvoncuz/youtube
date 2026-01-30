package dasturlash.uz.config;

import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // username = login or phone or email
        Optional<ProfileEntity> optional = profileRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        ProfileEntity profile = optional.get();
        return new CustomUserDetails(profile);
    }
}