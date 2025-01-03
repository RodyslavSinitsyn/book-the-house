package bth.postservice.repo.jpa;

import bth.postservice.entity.PostSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostSubscriptionRepository extends JpaRepository<PostSubscription, UUID> {
    List<PostSubscription> findAllByUserIdOrEmail(String userId, String email);
    List<PostSubscription> findAllBySubscribedUserIdAndEnabled(String subscribedUserId, boolean enabled);
    @Query("SELECT p FROM PostSubscription p WHERE p.subscribedUserId = :subscribedUserId AND (p.email = :email OR p.userId = :userId)")
    Optional<PostSubscription> findBySubscribedUserIdAndEmailOrUserId(@Param("subscribedUserId") String subscribedUserId,
                                                                      @Param("email") String email,
                                                                      @Param("userId") String userId);
}
