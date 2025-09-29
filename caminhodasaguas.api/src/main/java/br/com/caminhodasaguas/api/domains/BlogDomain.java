package br.com.caminhodasaguas.api.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.caminhodasaguas.api.domains.items.ItemDomainBlog;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "blogs")
public class BlogDomain extends BaseDomain {
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

    @OneToMany(mappedBy = "blogDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDomainBlog> highlights = new ArrayList<>();

    public BlogDomain() {}

    public static BlogDomain draft(String name, String description, String phone, String instagram, String site, String url) {
        BlogDomain blog = new BlogDomain();
        blog.setName(name);
        blog.setDescription(description);
        blog.setPhone(phone);
        blog.setInstagram(instagram);
        blog.setSite(site);
        blog.setUrl(url);
        return blog;
    }

    public static BlogDomain edit(BlogDomain blogDomain, String name, String description, String phone, String instagram, String site, String url){
        blogDomain.setName(name);
        blogDomain.setDescription(description);
        blogDomain.setPhone(phone);
        blogDomain.setInstagram(instagram);
        blogDomain.setSite(site);
        blogDomain.setUrl(url);
        return  blogDomain;
    }

    public void addHighlights(String img) {
        ItemDomainBlog item = ItemDomainBlog.draft(img);
        item.setBlogDomain(this);
        highlights.add(item);
    }

    public void removeHighlights(UUID id) {
        highlights.removeIf(item -> {
            boolean match = item.getId().equals(id);
            if (match) item.setBlogDomain(null);
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

    public List<ItemDomainBlog> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<ItemDomainBlog> highlights) {
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
