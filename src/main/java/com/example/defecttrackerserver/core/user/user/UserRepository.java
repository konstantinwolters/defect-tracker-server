package com.example.defecttrackerserver.core.user.user;

import com.example.defecttrackerserver.core.location.Location;
import com.example.defecttrackerserver.core.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String username);

    @Query("SELECT u FROM User u WHERE u.id IN :userIds")
    List<User> findByIds(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT a.isActive FROM User a WHERE a.id IN :userIds")
    Set<Boolean> findDistinctIsActive(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT a.location FROM User a WHERE a.id IN :userIds")
    Set<Location> findDistinctLocation(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT r FROM User u Join u.roles r WHERE u.id IN :userIds")
    Set<Role> findDistinctRoles(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT a.createdAt FROM User a WHERE a.id IN :userIds")
    Set<LocalDateTime> findDistinctCreatedAt(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT a.changedAt FROM User a WHERE a.id IN :userIds")
    Set<LocalDateTime> findDistinctChangedAt(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT a.createdById FROM User a WHERE a.id IN :userIds")
    List<Integer> findDistinctCreatedBy(@Param("userIds") List<Integer> userIds);

    @Query("SELECT DISTINCT a.changedById FROM User a WHERE a.id IN :userIds")
    List<Integer> findDistinctChangedBy(@Param("userIds") List<Integer> userIds);

    Set<User> findByLocationId(Integer id);

    Set<User> findByCreatedById(Integer id);
    Set<User> findByChangedById(Integer id);
}
