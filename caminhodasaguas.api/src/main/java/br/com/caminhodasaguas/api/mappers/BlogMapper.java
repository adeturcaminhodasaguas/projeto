package br.com.caminhodasaguas.api.mappers;

import br.com.caminhodasaguas.api.DTO.BlogDTO;
import br.com.caminhodasaguas.api.domains.BlogDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemBlogMapper;

import java.util.List;

public class BlogMapper {

    public static BlogDTO toDTO(BlogDomain blogDomain) {
        return new BlogDTO(
                blogDomain.getId(),
                blogDomain.getName(),
                blogDomain.getDescription(),
                blogDomain.getPhone(),
                blogDomain.getInstagram(),
                blogDomain.getSite(),
                ItemBlogMapper.toDtoList(blogDomain.getHighlights()),
                null,
                null);
    }

    public static List<BlogDTO> toDTOList(List<BlogDomain> blogDomains) {
        return blogDomains.stream()
                .map(BlogMapper::toDTO)
                .toList();
    }
}