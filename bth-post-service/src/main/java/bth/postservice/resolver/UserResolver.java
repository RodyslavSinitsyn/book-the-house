package bth.postservice.resolver;

import bth.common.contract.UserService;
import bth.postservice.entity.User;
import bth.postservice.repo.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserResolver implements UserService {

    private final UserRepository userRepository;

    @MutationMapping
    @Override
    public String registerIfNotExist(@Argument("username") String username,
                                     @Argument("friendlyUsername") String friendlyUsername,
                                     @Argument("email") String email) {
        if (userRepository.existsByUsername(username)) {
            log.debug("User {} already exists", username);
            return "exist";
        }
        var user = new User();
        user.setUsername(username);
        user.setFriendlyName(friendlyUsername);
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now());
        var saved = userRepository.save(user);
        log.debug("Registered new user {}", saved);
        return saved.getId().toString();
    }
}
