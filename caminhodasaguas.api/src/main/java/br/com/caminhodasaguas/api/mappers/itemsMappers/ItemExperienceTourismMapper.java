package br.com.caminhodasaguas.api.mappers.itemsMappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import br.com.caminhodasaguas.api.domains.items.ItemDomainExperienceTourism;

public class ItemExperienceTourismMapper {
    public static ItemCustomDTO toDto(ItemDomainExperienceTourism itemDomain){
        return new ItemCustomDTO(
                itemDomain.getId(),
                itemDomain.getImg()
        );
    }

    public static List<ItemCustomDTO> toDtoList(List<ItemDomainExperienceTourism> itemDomains){
        return itemDomains.stream()
                .map(ItemExperienceTourismMapper::toDto)
                .toList();
    }
}
