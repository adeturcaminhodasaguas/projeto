package br.com.caminhodasaguas.api.mappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.customs.BannerCustomDTO;
import br.com.caminhodasaguas.api.domains.BannerDomain;

public class BannerMapper {
    
    public static BannerCustomDTO toDTO(BannerDomain bannerDomain){
        return new BannerCustomDTO(
                bannerDomain.getId(),
                bannerDomain.getImg(),
                bannerDomain.getPosition(),
                bannerDomain.getLink(),
                bannerDomain.getAltText()
        );
    }

    public static List<BannerCustomDTO> toDTOList(List<BannerDomain> bannerDomains){
        return bannerDomains.stream()
                .map(BannerMapper::toDTO)
                .toList();
    }
}
