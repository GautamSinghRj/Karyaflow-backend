package com.example.karyaflow.repo;

import com.example.karyaflow.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat,Long> {
    List<Chat> findAllByOrderByTimestampAsc();
}
