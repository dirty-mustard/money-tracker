package moneytracker.security.response;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.views.RefreshTokenView;
import moneytracker.views.UserTokenView;

public class RefreshTokenResponse {

    @JsonView({RefreshTokenView.class, UserTokenView.class})
    private String status;

    private RefreshTokenResponse(String status) {
        this.status = status;
    }

    public static RefreshTokenResponse success() {
        return new RefreshTokenResponse("success");
    }

    public String status() {
        return status;
    }
}
