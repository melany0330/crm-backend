package org.wms.repository.campaign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.campaign.CampaignClient;
import java.util.List;

public interface CampaignClientRepository extends JpaRepository<CampaignClient, Integer> {

    // Busca clientes por campaña usando el nombre del atributo del entity
    List<CampaignClient> findByCampaign_IdCampaign(Integer campaignId);

    // Busca campañas por cliente usando el nombre del atributo del entity
    List<CampaignClient> findByClient_IdClient(Integer clientId);
}
