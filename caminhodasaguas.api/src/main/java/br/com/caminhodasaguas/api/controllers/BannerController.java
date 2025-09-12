package br.com.caminhodasaguas.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.customs.BannerCustomDTO;
import br.com.caminhodasaguas.api.DTO.request.BannerEditRequestDTO;
import br.com.caminhodasaguas.api.DTO.request.BannerRequestDTO;
import br.com.caminhodasaguas.api.services.BannerService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<BannerCustomDTO>> save(@ModelAttribute @Valid BannerRequestDTO bannerRequestDTO) {
        ResponseDTO<BannerCustomDTO> banner = bannerService.save(bannerRequestDTO);
        return ResponseEntity.ok(banner);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<BannerCustomDTO>> update(@PathVariable("id") UUID id, @ModelAttribute @Valid BannerEditRequestDTO bannerEditRequestDTO) {
        ResponseDTO<BannerCustomDTO> banner = bannerService.update(id, bannerEditRequestDTO);
        return ResponseEntity.ok(banner);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<BannerCustomDTO>>> findAll() {
        ResponseDTO<List<BannerCustomDTO>> banners = bannerService.findAll();
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<BannerCustomDTO>> findById(@PathVariable("id") UUID id) {
        ResponseDTO<BannerCustomDTO> banner = bannerService.findById(id);
        return ResponseEntity.ok(banner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        bannerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
