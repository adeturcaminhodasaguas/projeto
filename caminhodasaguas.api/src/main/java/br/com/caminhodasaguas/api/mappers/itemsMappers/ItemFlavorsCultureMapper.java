package br.com.caminhodasaguas.api.mappers.itemsMappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import br.com.caminhodasaguas.api.domains.items.ItemDomainFlavorsCulture;

public class ItemFlavorsCultureMapper {
     public static ItemCustomDTO toDto(ItemDomainFlavorsCulture itemDomain){
        return new ItemCustomDTO(
                itemDomain.getId(),
                itemDomain.getImg()
        );
    }

    public static List<ItemCustomDTO> toDtoList(List<ItemDomainFlavorsCulture> itemDomains){
        return itemDomains.stream()
                .map(ItemFlavorsCultureMapper::toDto)
                .toList();
    }
}
