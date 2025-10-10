package org.wms.dto.purchases;
import org.wms.model.purchases.Provider;
import org.wms.model.purchases.Purchase;

import java.util.List;

public class ProviderDto {
    private Integer idProvider;
    private String nit;
    private String name;
    private String email;
    private String phone;
    private String address;

    public ProviderDto() {
    }

    public ProviderDto(Provider provider) {
        this.idProvider = provider.getIdProvider();
        this.nit = provider.getNit();
        this.name = provider.getName();
        this.email = provider.getEmail();
        this.phone = provider.getPhone();
        this.address = provider.getAddress();


    }

    public Integer getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(Integer idProvider) {
        this.idProvider = idProvider;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

}
