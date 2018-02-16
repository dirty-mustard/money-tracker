package moneytracker.security.response;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.model.ApplicationUser;
import moneytracker.views.UserTokenView;

public class UserTokenResponse {

    @JsonView(UserTokenView.class)
    private RefreshTokenResponse status;

    @JsonView(UserTokenView.class)
    private ApplicationUser user;

    private UserTokenResponse(RefreshTokenResponse status, ApplicationUser user) {
        this.status = status;
        this.user = user;
    }

    public static UserTokenResponse successFromUser(ApplicationUser user) {
        return new UserTokenResponse(
                RefreshTokenResponse.success(),
                user
        );
    }

    public RefreshTokenResponse status() {
        return status;
    }

    public ApplicationUser getUser() {
        return user;
    }
}
