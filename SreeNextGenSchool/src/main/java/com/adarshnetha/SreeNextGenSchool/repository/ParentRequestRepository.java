package com.adarshnetha.SreeNextGenSchool.repository;

import com.adarshnetha.SreeNextGenSchool.entity.ParentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRequestRepository extends JpaRepository<ParentRequest, Long> {

    boolean existsByPhoneWhatsApp(String phoneWhatsApp);
}
