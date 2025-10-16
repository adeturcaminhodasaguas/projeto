package br.com.caminhodasaguas.api.controllers;

import br.com.caminhodasaguas.api.DTO.EventDTO;
import br.com.caminhodasaguas.api.DTO.PageResponseDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.services.EventService;
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
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<EventDTO>> save(@ModelAttribute @Valid EventDTO eventDTO) throws IOException {
        ResponseDTO<EventDTO> event = eventService.save(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<EventDTO>> update(@PathVariable("id") UUID id,
            @ModelAttribute @Valid EventDTO eventDTO) throws IOException {
        ResponseDTO<EventDTO> event = eventService.update(id, eventDTO);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EventDTO>>> findAll() {
        ResponseDTO<List<EventDTO>> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<EventDTO>> findById(@PathVariable("id") UUID id) {
        ResponseDTO<EventDTO> event = eventService.findById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/web")
    public ResponseEntity<PageResponseDTO<EventDTO>> findAllWeb(Pageable pageable) {
        PageResponseDTO<EventDTO> events = eventService.findAllWeb(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/web/highlights")
    public ResponseEntity<ResponseDTO<List<EventDTO>>> findByHighlightTrue() {
        ResponseDTO<List<EventDTO>> events = eventService.findByHighlightTrue();
        return ResponseEntity.ok(events);
    }
}
