package com.ctw.workstation.rack.service;

import com.ctw.workstation.exception.ConflictingRackException;
import com.ctw.workstation.exception.TeamNotFoundException;
import com.ctw.workstation.rack.dto.RackRequest;
import com.ctw.workstation.rack.dto.RackResponse;
import com.ctw.workstation.rack.entity.Rack;
import com.ctw.workstation.rack.repository.RackRepository;
import com.ctw.workstation.team.entity.Team;
import com.ctw.workstation.team.service.TeamService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RackService {
    @Inject
    RackRepository repo;

    @Inject
    TeamService teamService;

    @Transactional
    public RackResponse create(RackRequest request)
            throws TeamNotFoundException, ConflictingRackException {
        Rack rack = new Rack();
        setRackFromRequest(rack, request);
        repo.persist(rack);
        repo.flush();
        return RackResponse.from(rack);
    }

    @Transactional
    public List<RackResponse> getAll() {
        return repo.findAll().stream().sorted((one, other) -> (int) (one.getId() - other.getId())).map(RackResponse::from).toList();
    }

    @Transactional
    public Optional<RackResponse> getByIdResponse(Long id) {
        Optional<Rack> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Rack rack = fetched.get();
        return Optional.of(RackResponse.from(rack));
    }

    @Transactional
    public Optional<RackResponse> getBySerialNumberResponse(String serialNumber) {
        Optional<Rack> fetched = getBySerialNumber(serialNumber);
        if (fetched.isEmpty()) return Optional.empty();
        Rack rack = fetched.get();
        return Optional.of(RackResponse.from(rack));
    }

    @Transactional
    public Optional<RackResponse> update(Long id, RackRequest request)
            throws TeamNotFoundException, ConflictingRackException {
        Optional<Rack> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Rack rack = fetched.get();
        setRackFromRequest(rack, request);
        repo.persist(rack);
        repo.flush();
        return Optional.of(RackResponse.from(rack));
    }

    @Transactional
    public Optional<RackResponse> delete(Long id) {
        Optional<Rack> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Rack rack = fetched.get();
        repo.delete(rack);
        repo.flush();
        return Optional.of(RackResponse.from(rack));
    }

    @Transactional
    public Optional<Rack> getById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    @Transactional
    public Optional<Rack> getBySerialNumber(String serialNumber) {
        return Optional.ofNullable(repo.findBySerialNumber(serialNumber));
    }

    private void setRackFromRequest(Rack entity, RackRequest request)
            throws TeamNotFoundException, ConflictingRackException {
        Optional<Team> teamOptional = teamService.getById(request.teamId());
        if (teamOptional.isEmpty()) throw new TeamNotFoundException(request.teamId());

        Optional<Rack> conflicting = getBySerialNumber(request.serialNumber());
        if (conflicting.isPresent() && conflicting.get().getId() != entity.getId()) throw new ConflictingRackException(request.serialNumber());

        Team team = teamOptional.get();

        entity.setSerialNumber(request.serialNumber());
        entity.setStatus(request.status());
        entity.setTeam(team);
        entity.setDefaultLocation(request.defaultLocation());
        repo.persist(entity);
        repo.flush();
    }
}
