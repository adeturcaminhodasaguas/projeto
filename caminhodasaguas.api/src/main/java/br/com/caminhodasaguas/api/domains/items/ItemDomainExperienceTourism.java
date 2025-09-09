package br.com.caminhodasaguas.api.domains.items;

import br.com.caminhodasaguas.api.domains.BaseDomain;
import br.com.caminhodasaguas.api.domains.ExperienceTourismDomain;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class ItemDomainExperienceTourism extends BaseDomain {

    @Column(nullable = false)
    private String img;

    @ManyToOne(optional = false)
    private ExperienceTourismDomain experienceTourismDomain;

    public ItemDomainExperienceTourism() {}

    public static ItemDomainExperienceTourism draft(String img){
        ItemDomainExperienceTourism item = new ItemDomainExperienceTourism();
        item.setImg(img);
        return item;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ExperienceTourismDomain getExperienceTourismDomain() {
        return experienceTourismDomain;
    }

    public void setExperienceTourismDomain(ExperienceTourismDomain experienceTourismDomain) {
        this.experienceTourismDomain = experienceTourismDomain;
    }
}
