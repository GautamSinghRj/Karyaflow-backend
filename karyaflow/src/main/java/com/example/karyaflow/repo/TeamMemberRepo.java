package com.example.karyaflow.repo;

import com.example.karyaflow.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepo extends JpaRepository<TeamMember,Long> {
//no custom sql here
}
