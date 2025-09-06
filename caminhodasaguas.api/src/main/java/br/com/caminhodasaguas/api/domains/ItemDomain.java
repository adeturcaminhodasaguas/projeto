package br.com.caminhodasaguas.api.domains;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class ItemDomain extends BaseDomain {

    @Column(nullable = false)
    private String img;

    @ManyToOne(optional = false)
    private MunicipalityDomain municipalityDomain;

    public ItemDomain() {}

    public static ItemDomain draft(String img){
        ItemDomain item = new ItemDomain();
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
