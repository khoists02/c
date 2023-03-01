package com.school.management.application.api;

import com.school.management.GenericProto;
import com.school.management.PlaystationsProto;
import com.school.management.application.criteria.PlaystationsSearchCriteria;
import com.school.management.application.model.Playstation;
import com.school.management.application.services.PlayStationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.io.InvalidObjectException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PlayStationService playStationService;

    @GetMapping("playstations")
    public ResponseEntity<List<Playstation>> getPlaystationsInRange(@RequestParam("date") String date) throws HttpServerErrorException, DateTimeParseException {
        ZonedDateTime currentDate = ZonedDateTime.parse(date);
        return new ResponseEntity<>(playStationService.getListPlaystationsInRange(currentDate), HttpStatus.OK);
    }


    @PostMapping("/playstations")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaystationsProto.PlaystationResponse addNewPlaystation (@Valid @RequestBody PlaystationsProto.PlaystationRequest model) throws InvalidObjectException {
        Playstation playstation = playStationService.insertPlayStation(mapper.map(model, Playstation.class));
        return PlaystationsProto.PlaystationResponse.newBuilder()
                .setCleaning(playstation.getCleaning())
                .setId(String.format(playstation.getId().toString()))
                .setCleaning(playstation.getCleaning())
                .setMatu(playstation.getMatu())
                .setSmoke(playstation.getSmoke())
                .setType(playstation.getType().toString()).build();
    }

    @GetMapping("/getAll")
    public PlaystationsProto.PlaystationResponses getAll(
            @RequestParam(name = "keyword") Optional<String> keyword,
            @PageableDefault(sort = "smoke") Pageable pageable
        ) {
        PlaystationsSearchCriteria playstationsSearchCriteria = new PlaystationsSearchCriteria();
        playstationsSearchCriteria.setKeyword(keyword.orElse(null));
        Page<Playstation> playstations =  playStationService.finAll(pageable, playstationsSearchCriteria);
        return PlaystationsProto.PlaystationResponses.newBuilder()
                .addAllContent(
                    playstations.stream().map(playstation -> PlaystationsProto.PlaystationResponse.newBuilder()
                            .setCleaning(playstation.getCleaning())
                            .setId(String.format(playstation.getId().toString()))
                            .setCleaning(playstation.getCleaning())
                            .setMatu(playstation.getMatu())
                            .setSmoke(playstation.getSmoke())
                            .setType(playstation.getType().toString()).build())
                            .toList())
                .setPageable(mapper.map(playstations, GenericProto.PageableResponse.Builder.class))
                .build();
    }
}
