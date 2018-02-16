package moneytracker.controllers;

import moneytracker.model.ApplicationUser;
import moneytracker.repositories.UserRepository;
import moneytracker.validation.Adding;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static moneytracker.security.SecurityConstants.SIGN_UP_URL;

@RestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(SIGN_UP_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Validated(Adding.class) ApplicationUser user) {
        ApplicationUser targetUser = new ApplicationUser();
        BeanUtils.copyProperties(user, targetUser, "id", "createdAt");

        targetUser.setPassword(bCryptPasswordEncoder.encode(targetUser.getPassword()));
        userRepository.save(targetUser);
    }

}
