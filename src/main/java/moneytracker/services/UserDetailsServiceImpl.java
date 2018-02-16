package moneytracker.services;

import moneytracker.model.ApplicationUser;
import moneytracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationApplicationUser = userRepository.getByUsername(username);
        if (applicationApplicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(
                applicationApplicationUser.getUsername(),
                applicationApplicationUser.getPassword(), emptyList());
    }

}
