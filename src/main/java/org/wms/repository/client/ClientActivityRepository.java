package org.wms.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.client.ClientActivity;
import java.util.List;

public interface ClientActivityRepository extends JpaRepository<ClientActivity, Integer> {

    List<ClientActivity> findByClientIdClient(Integer clientId);

    List<ClientActivity> findByUserId(Long userId);
}