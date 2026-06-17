package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findById(Long id);
    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    @Query(value = "SELECT * FROM users ORDER BY experience DESC LIMIT 10", nativeQuery = true)
    public List<User> topUsers();

    @Query("SELECT u.banned FROM User u WHERE u.userName = :login")
    boolean findBannedByUserName(@Param("login") String login);

}
