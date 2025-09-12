package br.com.caminhodasaguas.api.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.customs.ExperienceTourismCustomDTO;
import br.com.caminhodasaguas.api.DTO.request.ExperienceTourismEditRequestDTO;
import br.com.caminhodasaguas.api.DTO.request.ExperienceTourismRequestDTO;
import br.com.caminhodasaguas.api.services.ExperienceTourismService;

@RestController
@RequestMapping("/experience-tourism")
public class ExperienceTourismController {

    @Autowired
    private ExperienceTourismService experienceTourismService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ExperienceTourismCustomDTO>>> findAll() {
        ResponseDTO<List<ExperienceTourismCustomDTO>> response = experienceTourismService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ExperienceTourismCustomDTO>> findById(@PathVariable UUID id) {
        ResponseDTO<ExperienceTourismCustomDTO> response = experienceTourismService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ExperienceTourismCustomDTO>> save(
            @ModelAttribute ExperienceTourismRequestDTO experienceTourismRequestDTO) throws IOException {
        ResponseDTO<ExperienceTourismCustomDTO> response = experienceTourismService.save(experienceTourismRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ExperienceTourismCustomDTO>> update(
            @PathVariable("id") UUID id,
            @ModelAttribute ExperienceTourismEditRequestDTO experienceTourismEditRequestDTO) throws IOException {
        ResponseDTO<ExperienceTourismCustomDTO> response = experienceTourismService.update(id,
                experienceTourismEditRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        experienceTourismService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
