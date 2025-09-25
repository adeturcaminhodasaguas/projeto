package br.com.caminhodasaguas.api.mappers.itemsMappers;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import br.com.caminhodasaguas.api.domains.items.ItemDomainEvent;

import java.util.List;

public class ItemEventMapper {

    public static ItemCustomDTO toDto(ItemDomainEvent itemDomain) {
        return new ItemCustomDTO(
                itemDomain.getId(),
                itemDomain.getImg());
    }

    public static List<ItemCustomDTO> toDtoList(List<ItemDomainEvent> itemDomains) {
        return itemDomains.stream()
                .map(ItemEventMapper::toDto)
                .toList();
    }
}
