package br.com.caminhodasaguas.api.controllers;

import br.com.caminhodasaguas.api.DTO.request.MunicipalityEditRequestDTO;
import br.com.caminhodasaguas.api.DTO.request.MunicipalityRequestDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.customs.MunicipalityCustomDTO;
import br.com.caminhodasaguas.api.services.MunicipalityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseDTO<MunicipalityCustomDTO>> save(@ModelAttribute @Valid MunicipalityRequestDTO municipalityRequestDTO) throws IOException {
        ResponseDTO<MunicipalityCustomDTO> municipality = municipalityService.save(municipalityRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(municipality);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<MunicipalityCustomDTO>> update(@PathVariable("id") UUID id, @ModelAttribute @Valid MunicipalityEditRequestDTO municipalityEditRequestDTO) throws IOException {
        ResponseDTO<MunicipalityCustomDTO> municipality = municipalityService.update(id, municipalityEditRequestDTO);
        return ResponseEntity.ok(municipality);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        municipalityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MunicipalityCustomDTO>>> findAll(){
        ResponseDTO<List<MunicipalityCustomDTO>> municipalities = municipalityService.findAll();
        return ResponseEntity.ok(municipalities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MunicipalityCustomDTO>> findById(@PathVariable("id") UUID id){
        ResponseDTO<MunicipalityCustomDTO> municipality = municipalityService.findById(id);
        return ResponseEntity.ok(municipality);
    }
}
