package br.com.caminhodasaguas.api.domains.items;

import br.com.caminhodasaguas.api.domains.BaseDomain;
import br.com.caminhodasaguas.api.domains.MunicipalityDomain;
import jakarta.persistence.*;

@Entity
@Table(name = "items_municipality")
public class ItemDomainMunicipality extends BaseDomain {

    @Column(nullable = false)
    private String img;

    @ManyToOne(optional = false)
    private MunicipalityDomain municipalityDomain;

    public ItemDomainMunicipality() {}

    public static ItemDomainMunicipality draft(String img){
        ItemDomainMunicipality item = new ItemDomainMunicipality();
        item.setImg(img);
        return item;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MunicipalityDomain getMunicipalityDomain() {
        return municipalityDomain;
    }

    public void setMunicipalityDomain(MunicipalityDomain municipalityDomain) {
        this.municipalityDomain = municipalityDomain;
    }
}
