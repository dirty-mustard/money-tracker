package moneytracker.security;

import moneytracker.model.ApplicationUser;
import moneytracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContext {

    @Autowired
    private UserRepository userRepository;

    public ApplicationUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.getByUsername(authentication.getName());
    }

}
