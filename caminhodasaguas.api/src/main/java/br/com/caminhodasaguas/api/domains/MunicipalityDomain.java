package br.com.caminhodasaguas.api.domains;

import br.com.caminhodasaguas.api.DTO.request.MunicipalityEditRequestDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "municipalities")
public class MunicipalityDomain extends BaseDomain {

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

    @OneToMany(mappedBy = "municipalityDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDomain> highlights = new ArrayList<>();

    public MunicipalityDomain() {
    }

    public static MunicipalityDomain draft(String name, String description, String phone, String instagram, String site, String url) {
        MunicipalityDomain municipality = new MunicipalityDomain();
        municipality.setName(name);
        municipality.setDescription(description);
        municipality.setPhone(phone);
        municipality.setInstagram(instagram);
        municipality.setSite(site);
        municipality.setUrl(url);
        return municipality;
    }

    public static MunicipalityDomain edit(MunicipalityDomain municipalityDomain, String name, String description, String phone, String instagram, String site, String url){
        municipalityDomain.setName(name);
        municipalityDomain.setDescription(description);
        municipalityDomain.setPhone(phone);
        municipalityDomain.setInstagram(instagram);
        municipalityDomain.setSite(site);
        municipalityDomain.setUrl(url);
        return  municipalityDomain;
    }

    public void addHighlights(String img) {
        ItemDomain item = ItemDomain.draft(img);
        item.setMunicipalityDomain(this);
        highlights.add(item);
    }

    public void removeHighlights(UUID id) {
        highlights.removeIf(item -> {
            boolean match = item.getId().equals(id);
            if (match) item.setMunicipalityDomain(null);
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

    public List<ItemDomain> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<ItemDomain> highlights) {
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
