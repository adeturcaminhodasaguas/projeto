package br.com.caminhodasaguas.api.controllers;

import br.com.caminhodasaguas.api.DTO.BlogDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.services.BlogService;
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
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<BlogDTO>> save(@ModelAttribute @Valid BlogDTO blogDTO) throws IOException {
        ResponseDTO<BlogDTO> blog = blogService.save(blogDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(blog);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<BlogDTO>> update(@PathVariable("id") UUID id,
            @ModelAttribute @Valid BlogDTO blogDTO) throws IOException {
        ResponseDTO<BlogDTO> blog = blogService.update(id, blogDTO);
        return ResponseEntity.ok(blog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<BlogDTO>>> findAll() {
        ResponseDTO<List<BlogDTO>> blogs = blogService.findAll();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<BlogDTO>> findById(@PathVariable("id") UUID id) {
        ResponseDTO<BlogDTO> blog = blogService.findById(id);
        return ResponseEntity.ok(blog);
    }
}