package com.example.defecttrackerserver.core.user.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String username);

    @Query("SELECT u FROM User u WHERE u.id IN :userIds")
    List<User> findByIds(@Param("userIds") List<Integer> userIds);

    Set<User> findByLocationId(Integer id);

    Set<User> findByCreatedById(Integer id);
    Set<User> findByChangedById(Integer id);
}
