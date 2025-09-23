package br.com.caminhodasaguas.api.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.caminhodasaguas.api.domains.items.ItemDomainFlavorsCulture;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "flavors_culture")
public class FlavorsCultureDomain extends BaseDomain {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String phone;

    private String instagram;

    private String site;

    @Column(nullable = false)
    private String url;

    @OneToMany(mappedBy = "flavorsCultureDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDomainFlavorsCulture> highlights = new ArrayList<>();

    public FlavorsCultureDomain() {}

    public static FlavorsCultureDomain draft(String name, String description, String phone, String instagram, String site, String url) {
        FlavorsCultureDomain flavors = new FlavorsCultureDomain();
        flavors.setName(name);
        flavors.setDescription(description);
        flavors.setPhone(phone);
        flavors.setInstagram(instagram);
        flavors.setSite(site);
        flavors.setUrl(url);
        return flavors;
    }

    public static FlavorsCultureDomain edit(FlavorsCultureDomain flavorsCultureDomain, String name, String description, String phone, String instagram, String site, String url){
        flavorsCultureDomain.setName(name);
        flavorsCultureDomain.setDescription(description);
        flavorsCultureDomain.setPhone(phone);
        flavorsCultureDomain.setInstagram(instagram);
        flavorsCultureDomain.setSite(site);
        flavorsCultureDomain.setUrl(url);
        return  flavorsCultureDomain;
    }

    public void addHighlights(String img) {
        ItemDomainFlavorsCulture item = ItemDomainFlavorsCulture.draft(img);
        item.setFlavorsCultureDomain(this);
        highlights.add(item);
    }

    public void removeHighlights(UUID id) {
        highlights.removeIf(item -> {
            boolean match = item.getId().equals(id);
            if (match) item.setFlavorsCultureDomain(null);
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

    public List<ItemDomainFlavorsCulture> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<ItemDomainFlavorsCulture> highlights) {
        this.highlights = highlights;
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
