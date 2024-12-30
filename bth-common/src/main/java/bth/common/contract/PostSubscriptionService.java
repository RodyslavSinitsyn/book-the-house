package bth.common.contract;

import bth.common.dto.PostSubscriptionDto;

import java.util.List;

public interface PostSubscriptionService {
    List<PostSubscriptionDto> subscriptions(String userId, String email);
    PostSubscriptionDto subscribe(String subscribedUserId, String email, String userId);
    PostSubscriptionDto unsubscribe(String subscribedUserId, String email, String userId);
}
