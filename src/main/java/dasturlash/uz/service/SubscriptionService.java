package dasturlash.uz.service;

import dasturlash.uz.dto.SubscriptionInfo;
import dasturlash.uz.entity.SubscriptionEntity;
import dasturlash.uz.enums.Lang;
import dasturlash.uz.enums.NotificationType;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exception.AppBadException;
import dasturlash.uz.repository.SubscriptionRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private MessageSource messageSource;

    public void create(String channelId, NotificationType type){
        String profileId = SpringSecurityUtil.currentProfileId();
        Optional<SubscriptionEntity> subscriptionCheck = subscriptionRepository.findSubscriptionEntityByChannelIdAndProfileId(channelId, profileId);

        if (subscriptionCheck.isPresent()){
            return;
        }

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setProfileId(profileId);
        subscription.setChannelId(channelId);
        subscription.setNotificationType(type);
        subscription.setStatus(Status.ACTIVE);
        subscriptionRepository.save(subscription);
    }

    public void changeStatus(String channelId, Status status, Lang lang){
        String profileId = SpringSecurityUtil.currentProfileId();
        int check = subscriptionRepository.updateStatus(profileId, channelId, status);
        if (check != 1){
            throw new AppBadException(messageSource.getMessage("subscription.not.found", null, new Locale(lang.name())));
        }
    }

    public void changeNotificationType(String channelId, NotificationType type, Lang lang){
        String profileId = SpringSecurityUtil.currentProfileId();
        int check = subscriptionRepository.updateNotification(profileId, channelId, type);
        if (check != 1){
            throw new AppBadException(messageSource.getMessage("subscription.not.found", null, new Locale(lang.name())));
        }
    }

    public List<SubscriptionInfo> getAllSubscriptions(){
        String profileId = SpringSecurityUtil.currentProfileId();
        return subscriptionRepository.findAllByProfileIdAndStatusIs(profileId);
    }
}
