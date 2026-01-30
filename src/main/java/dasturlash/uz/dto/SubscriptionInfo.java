package dasturlash.uz.dto;

import dasturlash.uz.enums.NotificationType;

public interface SubscriptionInfo {
    String getId();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    String getChannelPhotoUrl();
    NotificationType getNotificationType();
}
