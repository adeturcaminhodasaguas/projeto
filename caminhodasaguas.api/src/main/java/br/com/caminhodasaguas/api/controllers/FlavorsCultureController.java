package br.com.caminhodasaguas.api.controllers;

import br.com.caminhodasaguas.api.DTO.FlavorsCultureDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.services.FlavorsCultureService;
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
@RequestMapping("/flavors-culture")
public class FlavorsCultureController {

    @Autowired
    private FlavorsCultureService flavorsCultureService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<FlavorsCultureDTO>> save(@ModelAttribute @Valid FlavorsCultureDTO flavorsCultureDTO) throws IOException {
        ResponseDTO<FlavorsCultureDTO> flavorsCulture = flavorsCultureService.save(flavorsCultureDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(flavorsCulture);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<FlavorsCultureDTO>> update(@PathVariable("id") UUID id, @ModelAttribute @Valid FlavorsCultureDTO flavorsCultureDTO) throws IOException {
        ResponseDTO<FlavorsCultureDTO> flavorsCulture = flavorsCultureService.update(id, flavorsCultureDTO);
        return ResponseEntity.ok(flavorsCulture);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        flavorsCultureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<FlavorsCultureDTO>>> findAll(){
        ResponseDTO<List<FlavorsCultureDTO>> flavorsCultures = flavorsCultureService.findAll();
        return ResponseEntity.ok(flavorsCultures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<FlavorsCultureDTO>> findById(@PathVariable("id") UUID id){
        ResponseDTO<FlavorsCultureDTO> flavorsCulture = flavorsCultureService.findById(id);
        return ResponseEntity.ok(flavorsCulture);
    }
}
