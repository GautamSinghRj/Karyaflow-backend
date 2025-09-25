package com.example.karyaflow.repo;

import com.example.karyaflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {
    //no custom sql needed
}
