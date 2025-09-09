package br.com.caminhodasaguas.api.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.caminhodasaguas.api.domains.items.ItemDomainExperienceTourism;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "experience_tourism")
public class ExperienceTourismDomain extends BaseDomain {
     @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String img;

    @Column(nullable = false)
    private String phone;

    private String instagram;

    private String site;

    @Column(nullable = false)
    private String url;

    @OneToMany(mappedBy = "experienceTourismDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDomainExperienceTourism> highlights = new ArrayList<>();

    public ExperienceTourismDomain() {
    }

    public static ExperienceTourismDomain draft(String name, String description, String phone, String instagram, String site, String url) {
        ExperienceTourismDomain experience = new ExperienceTourismDomain();
        experience.setName(name);
        experience.setDescription(description);
        experience.setPhone(phone);
        experience.setInstagram(instagram);
        experience.setSite(site);
        experience.setUrl(url);
        return experience;
    }

    public static ExperienceTourismDomain edit(ExperienceTourismDomain experienceTourism, String name, String description, String phone, String instagram, String site, String url){
        experienceTourism.setName(name);
        experienceTourism.setDescription(description);
        experienceTourism.setPhone(phone);
        experienceTourism.setInstagram(instagram);
        experienceTourism.setSite(site);
        experienceTourism.setUrl(url);
        return experienceTourism;
    }

    public void addHighlights(String img) {
        ItemDomainExperienceTourism item = ItemDomainExperienceTourism.draft(img);
        item.setExperienceTourismDomain(this);
        highlights.add(item);
    }

    public void removeHighlights(UUID id) {
        highlights.removeIf(item -> {
            boolean match = item.getId().equals(id);
            if (match) item.setExperienceTourismDomain(null);
            return match;
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ItemDomainExperienceTourism> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<ItemDomainExperienceTourism> highlights) {
        this.highlights = highlights;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
