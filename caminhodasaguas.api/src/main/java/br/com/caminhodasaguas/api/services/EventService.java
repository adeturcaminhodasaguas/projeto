package br.com.caminhodasaguas.api.services;

import br.com.caminhodasaguas.api.DTO.EventDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.configs.exceptions.EventAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.EventNotFoundException;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.EventDomain;
import br.com.caminhodasaguas.api.mappers.EventMapper;
import br.com.caminhodasaguas.api.repositories.EventRepository;
import br.com.caminhodasaguas.api.utils.FormatDescription;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;
import br.com.caminhodasaguas.api.utils.ValidationValueUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class EventService {

    Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private MinioService minioService;

    @Autowired
    private EventRepository eventRepository;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Value("${spring.image.max-size}")
    private Integer MAX_SIZE;

    public ResponseDTO<List<EventDTO>> findAll() {
        List<EventDomain> domains = eventRepository.findAll();
        return new ResponseDTO<List<EventDTO>>(EventMapper.toDTOList(domains));
    }

    public ResponseDTO<EventDTO> findById(UUID id) {
        EventDomain domain = existEvent(id);
        return new ResponseDTO<EventDTO>(EventMapper.toDTO(domain));
    }

    public void delete(UUID id) {
        EventDomain domain = existEvent(id);
        eventRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<EventDTO> update(UUID id, EventDTO eventEditRequestDTO) throws IOException {
        EventDomain event = existEvent(id);
        validation(eventEditRequestDTO.phone(), eventEditRequestDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(eventEditRequestDTO.name());

        EventDomain update = EventDomain.edit(
                event,
                eventEditRequestDTO.name(),
                FormatDescription.format(eventEditRequestDTO.description()),
                eventEditRequestDTO.phone(),
                eventEditRequestDTO.instagram(),
                eventEditRequestDTO.site(),
                url, 
                eventEditRequestDTO.municipality()
                );

        if (eventEditRequestDTO.deleted_highlights() != null) {
            update.getHighlights().removeIf(item -> {
                boolean match = eventEditRequestDTO.deleted_highlights().contains(item.getId());
                if (match)
                    item.setEventDomain(null);
                return match;
            });
        }

        if (eventEditRequestDTO.new_highlights() != null) {
            for (MultipartFile f : eventEditRequestDTO.new_highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        EventDomain save = eventRepository.save(event);
        return new ResponseDTO<EventDTO>(EventMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<EventDTO> save(EventDTO eventDTO) throws IOException {
        validation(eventDTO.phone(), eventDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(eventDTO.name());

        existEventUrl(url);

        EventDomain event = EventDomain.draft(
                eventDTO.name(),
                FormatDescription.format(eventDTO.description()),
                eventDTO.phone(),
                eventDTO.instagram(),
                eventDTO.site(),
                OnlyDigitsUtils.normalize(url),
                eventDTO.municipality()
                );

        eventDTO.new_highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        event.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        EventDomain save = eventRepository.save(event);
        return new ResponseDTO<EventDTO>(EventMapper.toDTO(save));
    }

    private void validation(String phone, List<MultipartFile> files) {
        if (!ValidationValueUtils.isValidAnyPhone(phone)) {
            logger.info("Phone is invalid with value: {}", phone);
            throw new PhoneInvalidException("Telefone inválido.");
        }

        if (files == null || files.isEmpty()) {
            return;
        }

        if (!ValidationValueUtils.isLengthValid(files, MAX_SIZE)) {
            logger.info("File is invalid with max size: {}", files.size());
            throw new MaxSizeInvalidException("Tamanho maximo 6 fotos.");
        }
    }

    private String upload(String bucketName, MultipartFile img) throws IOException {
        String name = OnlyDigitsUtils.normalize(img.getOriginalFilename()) + ".webp";
        return minioService.uploadFile(
                bucketName,
                name,
                img,
                "image/webp");
    }

    private EventDomain existEvent(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Evento não encontrado.");
                    throw new EventNotFoundException("Evento não encontrado.");
                });
    }

    private void existEventUrl(String url) {
        eventRepository.findByUrl(url)
                .ifPresent(eventDomain -> {
                    logger.info("Evento já cadastrado com o nome: {}", eventDomain.getName());
                    throw new EventAlreadyRegisteredException("Evento já cadastrado.");
                });
    }

}
