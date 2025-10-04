package br.com.caminhodasaguas.api.domains.items;

import br.com.caminhodasaguas.api.domains.BaseDomain;
import br.com.caminhodasaguas.api.domains.FlavorsCultureDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_flavors_culture")
public class ItemDomainFlavorsCulture extends BaseDomain {
    
     @Column(nullable = false)
    private String img;

    @ManyToOne(optional = false)
    private FlavorsCultureDomain flavorsCultureDomain;

    public ItemDomainFlavorsCulture() {}

    public static ItemDomainFlavorsCulture draft(String img){
        ItemDomainFlavorsCulture item = new ItemDomainFlavorsCulture();
        item.setImg(img);
        return item;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public FlavorsCultureDomain getFlavorsCultureDomain() {
        return flavorsCultureDomain;
    }

    public void setFlavorsCultureDomain(FlavorsCultureDomain flavorsCultureDomain) {
        this.flavorsCultureDomain = flavorsCultureDomain;
    }

}
