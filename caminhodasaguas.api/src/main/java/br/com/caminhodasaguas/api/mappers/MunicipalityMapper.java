package br.com.caminhodasaguas.api.mappers;

import br.com.caminhodasaguas.api.DTO.MunicipalityDTO;
import br.com.caminhodasaguas.api.domains.MunicipalityDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemMunicipalityMapper;

import java.util.List;

public class MunicipalityMapper {

    public static MunicipalityDTO toDTO(MunicipalityDomain municipalityDomain){
        return new MunicipalityDTO(
                municipalityDomain.getId(),
                municipalityDomain.getName(),
                municipalityDomain.getDescription(),
                municipalityDomain.getPhone(),
                municipalityDomain.getInstagram(),
                municipalityDomain.getSite(),
                ItemMunicipalityMapper.toDtoList(municipalityDomain.getHighlights()),
                null,
                null
        );
    }

    public static List<MunicipalityDTO> toDTOList(List<MunicipalityDomain> municipalityDomains){
        return municipalityDomains.stream()
                .map(MunicipalityMapper::toDTO)
                .toList();
    }
}
