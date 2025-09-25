package br.com.caminhodasaguas.api.domains.items;

import br.com.caminhodasaguas.api.domains.BaseDomain;
import br.com.caminhodasaguas.api.domains.EventDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items_event")
public class ItemDomainEvent extends BaseDomain {

    @Column(nullable = false)
    private String img;

    @ManyToOne(optional = false)
    private EventDomain eventDomain;

    public ItemDomainEvent() {
    }

    public static ItemDomainEvent draft(String img) {
        ItemDomainEvent item = new ItemDomainEvent();
        item.setImg(img);
        return item;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public EventDomain getEventDomain() {
        return eventDomain;
    }

    public void setEventDomain(EventDomain eventDomain) {
        this.eventDomain = eventDomain;
    }

}
