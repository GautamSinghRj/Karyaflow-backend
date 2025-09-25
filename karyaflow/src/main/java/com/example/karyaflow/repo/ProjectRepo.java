package com.example.karyaflow.repo;

import com.example.karyaflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepo extends JpaRepository<Project,Long> {
//no custom sql needed
}
