package br.com.caminhodasaguas.api.mappers;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import br.com.caminhodasaguas.api.domains.ItemDomain;

import java.util.List;

public class ItemMapper {

    public static ItemCustomDTO toDto(ItemDomain itemDomain){
        return new ItemCustomDTO(
                itemDomain.getId(),
                itemDomain.getImg()
        );
    }

    public static List<ItemCustomDTO> toDtoList(List<ItemDomain> itemDomains){
        return itemDomains.stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
