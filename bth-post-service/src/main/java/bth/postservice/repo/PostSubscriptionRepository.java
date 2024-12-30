package bth.postservice.repo;

import bth.postservice.entity.PostSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostSubscriptionRepository extends JpaRepository<PostSubscription, UUID> {
    List<PostSubscription> findAllBySubscribedUserIdAndEnabled(String subscribedUserId, boolean enabled);
}
