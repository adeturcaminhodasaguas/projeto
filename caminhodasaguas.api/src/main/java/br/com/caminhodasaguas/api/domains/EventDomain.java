package br.com.caminhodasaguas.api.domains;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.caminhodasaguas.api.domains.items.ItemDomainEvent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class EventDomain extends BaseDomain {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String phone;

    private String instagram;

    private String site;

    private String municipality;

    private String location;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false, name = "start_time")
    private String startTime;

    @Column(nullable = false, name = "end_time")
    private String endTime;

    private Boolean highlight = false;

    @Column(nullable = false)
    private String url;

    @OneToMany(mappedBy = "eventDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDomainEvent> highlights = new ArrayList<>();

    public EventDomain() {
    }

    public static EventDomain draft(String name, String description, String phone, String instagram, String site,
            String url, String municipality, String location, String date, String startTime, String endTime, Boolean highlight) {
        EventDomain event = new EventDomain();
        event.setName(name);
        event.setDescription(description);
        event.setPhone(phone);
        event.setInstagram(instagram);
        event.setSite(site);
        event.setUrl(url);
        event.setMunicipality(municipality);
        event.setLocation(location);
        event.setDate(date);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setHighlight(highlight);
        return event;
    }

    public static EventDomain edit(EventDomain eventDomain, String name, String description, String phone,
            String instagram, String site, String url, String municipality, String location, String date, String startTime,
            String endTime, Boolean highlight) {
        eventDomain.setName(name);
        eventDomain.setDescription(description);
        eventDomain.setPhone(phone);
        eventDomain.setInstagram(instagram);
        eventDomain.setSite(site);
        eventDomain.setUrl(url);
        eventDomain.setMunicipality(municipality);
        eventDomain.setLocation(location);
        eventDomain.setDate(date);
        eventDomain.setStartTime(startTime);
        eventDomain.setEndTime(endTime);
        eventDomain.setHighlight(highlight);
        return eventDomain;
    }

    public void addHighlights(String img) {
        ItemDomainEvent item = ItemDomainEvent.draft(img);
        item.setEventDomain(this);
        highlights.add(item);
    }

    public void removeHighlights(UUID id) {
        highlights.removeIf(item -> {
            boolean match = item.getId().equals(id);
            if (match)
                item.setEventDomain(null);
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

    public List<ItemDomainEvent> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<ItemDomainEvent> highlights) {
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

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }
}
