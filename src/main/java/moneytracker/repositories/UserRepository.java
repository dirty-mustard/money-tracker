package moneytracker.repositories;

import moneytracker.model.User;

public interface UserRepository {

    User getByUsername(String username);

}
