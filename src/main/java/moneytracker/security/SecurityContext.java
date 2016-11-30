package moneytracker.security;

import moneytracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SecurityContext {

    @Autowired
    private UserDetailsService userDetailsService;

    public User getAuthenticatedUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (User) userDetailsService.loadUserByUsername(authentication.getName());
        return (User) userDetailsService.loadUserByUsername("admin");
    }

}
