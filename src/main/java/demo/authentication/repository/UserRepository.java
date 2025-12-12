package demo.authentication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.authentication.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByOrderByUsernameAsc();
}
