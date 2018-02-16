package moneytracker.repositories;

import moneytracker.model.ApplicationUser;

public interface UserRepository {

    ApplicationUser getByUsername(String username);

    void save(ApplicationUser applicationUser);

}
