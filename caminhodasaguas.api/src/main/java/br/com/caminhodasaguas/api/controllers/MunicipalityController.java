package br.com.caminhodasaguas.api.controllers;

import br.com.caminhodasaguas.api.DTO.MunicipalityDTO;
import br.com.caminhodasaguas.api.DTO.PageResponseDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.services.MunicipalityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    @Autowired
    private MunicipalityService municipalityService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<MunicipalityDTO>> save(@ModelAttribute @Valid MunicipalityDTO municipalityDTO) throws IOException {
        ResponseDTO<MunicipalityDTO> municipality = municipalityService.save(municipalityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(municipality);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<MunicipalityDTO>> update(@PathVariable("id") UUID id, @ModelAttribute @Valid MunicipalityDTO municipalityDTO) throws IOException {
        ResponseDTO<MunicipalityDTO> municipality = municipalityService.update(id, municipalityDTO);
        return ResponseEntity.ok(municipality);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        municipalityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MunicipalityDTO>>> findAll(){
        ResponseDTO<List<MunicipalityDTO>> municipalities = municipalityService.findAll();
        return ResponseEntity.ok(municipalities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MunicipalityDTO>> findById(@PathVariable("id") UUID id){
        ResponseDTO<MunicipalityDTO> municipality = municipalityService.findById(id);
        return ResponseEntity.ok(municipality);
    }

    @GetMapping("/web")
    public ResponseEntity<PageResponseDTO<MunicipalityDTO>> findAllWeb(Pageable pageable){
        PageResponseDTO<MunicipalityDTO> municipalities = municipalityService.findAllWeb(pageable);
        return ResponseEntity.ok(municipalities);
    }
}