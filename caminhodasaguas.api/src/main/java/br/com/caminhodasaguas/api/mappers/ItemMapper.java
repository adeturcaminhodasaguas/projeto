package br.com.caminhodasaguas.api.mappers;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import br.com.caminhodasaguas.api.domains.items.ItemDomainMunicipality;

import java.util.List;

public class ItemMapper {

    public static ItemCustomDTO toDto(ItemDomainMunicipality itemDomain){
        return new ItemCustomDTO(
                itemDomain.getId(),
                itemDomain.getImg()
        );
    }

    public static List<ItemCustomDTO> toDtoList(List<ItemDomainMunicipality> itemDomains){
        return itemDomains.stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
