package br.com.caminhodasaguas.api.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.TestimonialsDTO;
import br.com.caminhodasaguas.api.services.TestimonialsService;

@RestController
@RequestMapping("/testimonials")
public class TestimonialsController {
    
    @Autowired
    private TestimonialsService testimonialsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<TestimonialsDTO>> save(@ModelAttribute TestimonialsDTO testimonialsDTO) throws IOException{
        ResponseDTO<TestimonialsDTO> testimonials = testimonialsService.save(testimonialsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(testimonials);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<TestimonialsDTO>> update(@PathVariable UUID id, @ModelAttribute TestimonialsDTO testimonialsDTO) throws IOException{
        ResponseDTO<TestimonialsDTO> testimonials = testimonialsService.update(id, testimonialsDTO);
        return ResponseEntity.ok(testimonials);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<TestimonialsDTO>>> findAll(){
        ResponseDTO<List<TestimonialsDTO>> testimonials = testimonialsService.findAll();
        return ResponseEntity.ok(testimonials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<TestimonialsDTO>> findById(@PathVariable UUID id){
        ResponseDTO<TestimonialsDTO> testimonials = testimonialsService.findById(id);
        return ResponseEntity.ok(testimonials);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        testimonialsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
