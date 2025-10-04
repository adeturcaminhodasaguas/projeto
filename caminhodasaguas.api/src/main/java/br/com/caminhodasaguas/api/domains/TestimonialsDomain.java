package br.com.caminhodasaguas.api.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "testimonials")
public class TestimonialsDomain extends BaseDomain {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    public Integer stars;

    @Column(nullable = false)
    private String img;

    public TestimonialsDomain() {}

    public static TestimonialsDomain draft(String name, String description, String city, Integer stars) {
        TestimonialsDomain testimonial = new TestimonialsDomain();
        testimonial.setName(name);
        testimonial.setDescription(description);
        testimonial.setCity(city);
        testimonial.setStars(stars);
        return testimonial;
    }

    public static TestimonialsDomain edit(TestimonialsDomain testimonialsDomain, String name, String description, String city, Integer stars){
        testimonialsDomain.setName(name);
        testimonialsDomain.setDescription(description);
        testimonialsDomain.setCity(city);
        testimonialsDomain.setStars(stars);
        return  testimonialsDomain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
