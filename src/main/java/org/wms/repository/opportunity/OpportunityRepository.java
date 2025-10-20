package org.wms.repository.opportunity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.opportunity.Opportunity;

public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

}