package br.com.caminhodasaguas.api.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "banners")
public class BannerDomain extends BaseDomain {

    @Column(nullable = false)
    private String img;

    @Column(nullable = false)
    public Integer position;

    public String link;

    public String altText;

    public BannerDomain() {
    }

    public static BannerDomain draft(String img, Integer position, String link, String altText) {
        BannerDomain banner = new BannerDomain();
        banner.setImg(img);
        banner.setPosition(position);
        banner.setLink(link);
        banner.setAltText(altText);
        return banner;
    }

    public static BannerDomain edit(BannerDomain bannerDomain, Integer position, String link, String altText) {
        bannerDomain.setPosition(position);
        bannerDomain.setLink(link);
        bannerDomain.setAltText(altText);
        return bannerDomain;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    

}
