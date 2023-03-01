package com.school.management.application.services;

import com.school.management.application.criteria.PlaystationsSearchCriteria;
import com.school.management.application.model.Playstation;
import com.school.management.application.repositories.PlaystationRepository;
import com.school.management.application.specifications.PlaystationSpecification;
import jakarta.persistence.EntityGraph;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.school.management.PlaystationsProto;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PlayStationService  {
    @Autowired
    private PlaystationRepository playstationRepository;

    @Autowired
    private ModelMapper mapper;
    public List<Playstation> getListPlaystationsInRange(ZonedDateTime date) {
        return playstationRepository.findAll();
    }

    public Playstation insertPlayStation(Playstation playstation) {
        Playstation model = new Playstation();
        model.setSmoke(playstation.getSmoke());
        model.setType(playstation.getType());
        model.setCleaning(playstation.getCleaning());
        model.setLearning(playstation.getLearning());
        model.setMatu(playstation.getMatu());
        model.setDate(ZonedDateTime.now());
        return playstationRepository.save(model);
    }

    public Playstation insertWithProto (PlaystationsProto.PlaystationRequest model) {
         Playstation newModel = mapper.map(model, Playstation.class);
        return playstationRepository.save(newModel);
    }

    public Page<Playstation> finAll(Pageable pageable, PlaystationsSearchCriteria playstationsSearchCriteria) {
        EntityGraph<Playstation> entityGraph = playstationRepository.createEntityGraph();
//        entityGraph.addAttributeNodes("name");
        return playstationRepository.findAll(PlaystationSpecification.playstationQuery(playstationsSearchCriteria), pageable, entityGraph);
    }
}
