package Togefit.server.repository;

import Togefit.server.domain.User;

import java.util.Optional;

public interface UserRepositoryInterface {
    User save(User user);
    Optional<User> findByUserId(String userId);
    void delete(User user);
}
