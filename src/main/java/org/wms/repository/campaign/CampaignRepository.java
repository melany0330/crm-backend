package org.wms.repository.campaign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.campaign.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Integer>{

}
