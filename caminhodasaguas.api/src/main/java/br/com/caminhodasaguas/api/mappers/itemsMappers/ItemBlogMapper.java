package br.com.caminhodasaguas.api.mappers.itemsMappers;

import br.com.caminhodasaguas.api.DTO.customs.ItemCustomDTO;
import br.com.caminhodasaguas.api.domains.items.ItemDomainBlog;

import java.util.List;

public class ItemBlogMapper {

    public static ItemCustomDTO toDto(ItemDomainBlog itemDomain) {
        return new ItemCustomDTO(
                itemDomain.getId(),
                itemDomain.getImg());
    }

    public static List<ItemCustomDTO> toDtoList(List<ItemDomainBlog> itemDomains) {
        return itemDomains.stream()
                .map(ItemBlogMapper::toDto)
                .toList();
    }
}