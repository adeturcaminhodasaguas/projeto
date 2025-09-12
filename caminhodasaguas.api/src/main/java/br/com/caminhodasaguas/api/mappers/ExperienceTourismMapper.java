package br.com.caminhodasaguas.api.mappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.customs.ExperienceTourismCustomDTO;
import br.com.caminhodasaguas.api.domains.ExperienceTourismDomain;
import br.com.caminhodasaguas.api.mappers.itemsMappers.ItemExperienceTourismMapper;

public class ExperienceTourismMapper {

    public static ExperienceTourismCustomDTO toDTO(ExperienceTourismDomain experienceTourismDomain) {
        return new ExperienceTourismCustomDTO(
                experienceTourismDomain.getId(),
                experienceTourismDomain.getName(),
                experienceTourismDomain.getDescription(),
                experienceTourismDomain.getImg(),
                experienceTourismDomain.getPhone(),
                experienceTourismDomain.getInstagram(),
                experienceTourismDomain.getSite(),
                ItemExperienceTourismMapper.toDtoList(experienceTourismDomain.getHighlights())
        );
    }

    public static List<ExperienceTourismCustomDTO> toDTOList(List<ExperienceTourismDomain> experienceTourismDomains) {
        return experienceTourismDomains.stream()
                .map(ExperienceTourismMapper::toDTO)
                .toList();
    }
}
