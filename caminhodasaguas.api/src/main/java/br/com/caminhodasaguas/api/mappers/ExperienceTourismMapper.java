package br.com.caminhodasaguas.api.mappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.ExperienceTourismDTO;
import br.com.caminhodasaguas.api.domains.ExperienceTourismDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemExperienceTourismMapper;

public class ExperienceTourismMapper {

    public static ExperienceTourismDTO toDTO(ExperienceTourismDomain experienceTourismDomain) {
        return new ExperienceTourismDTO(
                experienceTourismDomain.getId(),
                experienceTourismDomain.getName(),
                experienceTourismDomain.getDescription(),
                experienceTourismDomain.getPhone(),
                experienceTourismDomain.getInstagram(),
                experienceTourismDomain.getSite(),
                ItemExperienceTourismMapper.toDtoList(experienceTourismDomain.getHighlights()),
                null,
                null
        );
    }

    public static List<ExperienceTourismDTO> toDTOList(List<ExperienceTourismDomain> experienceTourismDomains) {
        return experienceTourismDomains.stream()
                .map(ExperienceTourismMapper::toDTO)
                .toList();
    }
}
