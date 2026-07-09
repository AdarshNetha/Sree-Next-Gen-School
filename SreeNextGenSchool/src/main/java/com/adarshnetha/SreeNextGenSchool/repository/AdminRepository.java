package com.adarshnetha.SreeNextGenSchool.repository;

import com.adarshnetha.SreeNextGenSchool.entity.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUsername(String username);
}
