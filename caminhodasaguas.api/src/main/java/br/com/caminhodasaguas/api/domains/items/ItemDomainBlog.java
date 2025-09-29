package br.com.caminhodasaguas.api.domains.items;

import br.com.caminhodasaguas.api.domains.BaseDomain;
import br.com.caminhodasaguas.api.domains.BlogDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_blog")
public class ItemDomainBlog extends BaseDomain {

    @Column(nullable = false)
    private String img;

    @ManyToOne(optional = false)
    private BlogDomain blogDomain;

    public ItemDomainBlog() {
    }

    public static ItemDomainBlog draft(String img) {
        ItemDomainBlog item = new ItemDomainBlog();
        item.setImg(img);
        return item;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BlogDomain getBlogDomain() {
        return blogDomain;
    }

    public void setBlogDomain(BlogDomain blogDomain) {
        this.blogDomain = blogDomain;
    }

}
