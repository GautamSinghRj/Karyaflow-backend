package com.example.karyaflow.repo;

import com.example.karyaflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query(value = "SELECT username FROM users",nativeQuery = true)
    List<String>findAllUsernames();
}
