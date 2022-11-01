package com.sstree.streaming.streamingserver.repository;

import com.sstree.streaming.streamingserver.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);
        boolean existsByUsername(String username);


}
