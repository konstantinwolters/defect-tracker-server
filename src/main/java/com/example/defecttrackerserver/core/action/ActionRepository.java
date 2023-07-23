package com.example.defecttrackerserver.core.action;

import com.example.defecttrackerserver.core.user.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ActionRepository extends JpaRepository<Action, Integer>, JpaSpecificationExecutor<Action> {
    List<Action> findByCreatedBy_Id(Integer userId);

    @Query("SELECT DISTINCT a.dueDate FROM Action a WHERE a.id IN :actionIds")
    Set<LocalDate> findDistinctDueDate(@Param("actionIds") List<Integer> actionIds);

    @Query("SELECT DISTINCT a.isCompleted FROM Action a WHERE a.id IN :actionIds")
    Set<Boolean> findDistinctIsCompleted(@Param("actionIds") List<Integer> actionIds);

    @Query("SELECT DISTINCT u FROM User u Join u.assignedActions a WHERE a.id IN :actionIds")
    Set<User> findDistinctAssignedUsers(@Param("actionIds") List<Integer> actionIds);

    @Query("SELECT DISTINCT a.defect.id FROM Action a WHERE a.id IN :actionIds")
    Set<Integer> findDistinctDefect(@Param("actionIds") List<Integer> actionIds);

    @Query("SELECT DISTINCT a.createdAt FROM Action a WHERE a.id IN :actionIds")
    Set<LocalDateTime> findDistinctCreatedOn(@Param("actionIds") List<Integer> actionIds);

    @Query("SELECT DISTINCT a.createdBy FROM Action a WHERE a.id IN :actionIds")
    Set<User> findDistinctCreatedBy(@Param("actionIds") List<Integer> actionIds);
}
