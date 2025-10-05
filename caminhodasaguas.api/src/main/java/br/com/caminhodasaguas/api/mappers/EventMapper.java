package br.com.caminhodasaguas.api.mappers;

import br.com.caminhodasaguas.api.DTO.EventDTO;
import br.com.caminhodasaguas.api.domains.EventDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemEventMapper;

import java.util.List;

public class EventMapper {

    public static EventDTO toDTO(EventDomain eventDomain) {
        return new EventDTO(
                eventDomain.getId(),
                eventDomain.getName(),
                eventDomain.getDescription(),
                eventDomain.getPhone(),
                eventDomain.getInstagram(),
                eventDomain.getSite(),
                ItemEventMapper.toDtoList(eventDomain.getHighlights()),
                eventDomain.getMunicipio(),
                null,
                null);
    }

    public static List<EventDTO> toDTOList(List<EventDomain> eventDomains) {
        return eventDomains.stream()
                .map(EventMapper::toDTO)
                .toList();
    }
}
