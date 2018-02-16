package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.UserRepository;
import moneytracker.security.SecurityContext;
import moneytracker.security.response.RefreshTokenResponse;
import moneytracker.security.response.UserTokenResponse;
import moneytracker.validation.Adding;
import moneytracker.views.RefreshTokenView;
import moneytracker.views.UserTokenView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static moneytracker.security.SecurityConstants.API_AUTH_REFRESH;
import static moneytracker.security.SecurityConstants.API_AUTH_SIGNUP;

@RestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecurityContext securityContext;

    @PostMapping(API_AUTH_SIGNUP)
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Validated(Adding.class) ApplicationUser user) {
        ApplicationUser targetUser = new ApplicationUser();
        BeanUtils.copyProperties(user, targetUser, "id", "createdAt");

        targetUser.setPassword(bCryptPasswordEncoder.encode(targetUser.getPassword()));
        userRepository.save(targetUser);
    }

    @GetMapping(API_AUTH_REFRESH)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(RefreshTokenView.class)
    public RefreshTokenResponse refresh() {
        return RefreshTokenResponse.success();
    }

    @GetMapping("/api/auth/user")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(UserTokenView.class)
    public UserTokenResponse userToken() {
        return UserTokenResponse.successFromUser(securityContext.getAuthenticatedUser());
    }

}
