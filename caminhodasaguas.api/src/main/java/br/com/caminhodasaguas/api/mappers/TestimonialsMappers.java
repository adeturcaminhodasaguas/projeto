package br.com.caminhodasaguas.api.mappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.TestimonialsDTO;
import br.com.caminhodasaguas.api.domains.TestimonialsDomain;

public class TestimonialsMappers {

    public static TestimonialsDTO toDTO(TestimonialsDomain testimonialsDomain) {
        return new TestimonialsDTO(
                testimonialsDomain.getId(),
                testimonialsDomain.getName(),
                testimonialsDomain.getDescription(),
                testimonialsDomain.getCity(),
                testimonialsDomain.getStars(),
                testimonialsDomain.getImg(),
                null);
    }

    public static List<TestimonialsDTO> toDTOList(List<TestimonialsDomain> domains) {
        return domains.stream()
                .map(TestimonialsMappers::toDTO)
                .toList();
    }

}
