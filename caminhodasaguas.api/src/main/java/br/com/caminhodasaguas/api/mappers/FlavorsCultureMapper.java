package br.com.caminhodasaguas.api.mappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.FlavorsCultureDTO;
import br.com.caminhodasaguas.api.domains.FlavorsCultureDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemFlavorsCultureMapper;

public class FlavorsCultureMapper {
    public static FlavorsCultureDTO toDTO(FlavorsCultureDomain flavorsCultureDomain){
        return new FlavorsCultureDTO(
                flavorsCultureDomain.getId(),
                flavorsCultureDomain.getName(),
                flavorsCultureDomain.getDescription(),
                flavorsCultureDomain.getPhone(),
                flavorsCultureDomain.getInstagram(),
                flavorsCultureDomain.getSite(),
                ItemFlavorsCultureMapper.toDtoList(flavorsCultureDomain.getHighlights()),
                flavorsCultureDomain.getMunicipality(),
                null,
                null
        );
    }

    public static List<FlavorsCultureDTO> toDTOList(List<FlavorsCultureDomain> flavorsCultureDomains){
        return flavorsCultureDomains.stream()
                .map(FlavorsCultureMapper::toDTO)
                .toList();
    }
}
