package org.wms.repository.purchases;
import org.wms.model.purchases.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

}