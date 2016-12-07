package moneytracker.security;

import moneytracker.model.User;
import moneytracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityContext {

    @Autowired
    private UserRepository userRepository;

    public User getAuthenticatedUser() {
        return userRepository.getByUsername("admin");
    }

}
