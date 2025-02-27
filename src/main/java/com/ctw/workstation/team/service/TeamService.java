package com.ctw.workstation.team.service;

import com.ctw.workstation.exception.ConflictingTeamException;
import com.ctw.workstation.team.dto.TeamRequest;
import com.ctw.workstation.team.dto.TeamResponse;
import com.ctw.workstation.team.entity.Team;
import com.ctw.workstation.team.repository.TeamRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TeamService {
    @Inject
    TeamRepository repo;

    @Transactional
    public TeamResponse create(TeamRequest request) {
        Team team = new Team();
        setTeamFromRequest(team, request);
        repo.persist(team);
        repo.flush();
        return TeamResponse.from(team);
    }

    @Transactional
    public List<TeamResponse> getAll() {
        return repo.findAll().stream().map(TeamResponse::from).toList();
    }

    public Optional<TeamResponse> getByIdResponse(Long id) {
        Optional<Team> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Team team = fetched.get();
        return Optional.of(TeamResponse.from(team));
    }

    @Transactional
    public Optional<TeamResponse> update(Long id, TeamRequest request) throws ConflictingTeamException {
        Optional<Team> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Team team = fetched.get();
        setTeamFromRequest(team, request);
        return Optional.of(TeamResponse.from(team));
    }

    @Transactional
    public Optional<TeamResponse> delete(Long id) {
        Optional<Team> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Team team = fetched.get();
        repo.delete(team);
        return Optional.of(TeamResponse.from(team));
    }

    @Transactional
    public Optional<Team> getById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    private void setTeamFromRequest(Team entity, TeamRequest request) throws ConflictingTeamException {
        Optional<Team> conflicting = getByName(request.name());
        if (conflicting.isPresent()) throw new ConflictingTeamException(request.name());
        entity.setName(request.name());
        entity.setProduct(request.product());
        entity.setDefaultLocation(request.defaultLocation());
    }

    @Transactional
    public Optional<Team> getByName(String name) {
        return Optional.ofNullable(repo.findByName(name));
    }
}
