package br.com.caminhodasaguas.api.mappers;

import br.com.caminhodasaguas.api.DTO.customs.MunicipalityCustomDTO;
import br.com.caminhodasaguas.api.domains.MunicipalityDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemMunicipalityMapper;

import java.util.List;

public class MunicipalityMapper {

    public static MunicipalityCustomDTO toDTO(MunicipalityDomain municipalityDomain){
        return new MunicipalityCustomDTO(
                municipalityDomain.getId(),
                municipalityDomain.getName(),
                municipalityDomain.getDescription(),
                municipalityDomain.getImg(),
                municipalityDomain.getPhone(),
                municipalityDomain.getInstagram(),
                municipalityDomain.getSite(),
                ItemMunicipalityMapper.toDtoList(municipalityDomain.getHighlights())
        );
    }

    public static List<MunicipalityCustomDTO> toDTOList(List<MunicipalityDomain> municipalityDomains){
        return municipalityDomains.stream()
                .map(MunicipalityMapper::toDTO)
                .toList();
    }
}
