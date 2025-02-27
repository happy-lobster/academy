package com.ctw.workstation.teammember.service;

import com.ctw.workstation.exception.ConflictingTeamMemberException;
import com.ctw.workstation.exception.TeamNotFoundException;
import com.ctw.workstation.team.entity.Team;
import com.ctw.workstation.team.service.TeamService;
import com.ctw.workstation.teammember.dto.TeamMemberRequest;
import com.ctw.workstation.teammember.dto.TeamMemberResponse;
import com.ctw.workstation.teammember.entity.TeamMember;
import com.ctw.workstation.teammember.repository.TeamMemberRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TeamMemberService {
    @Inject
    TeamMemberRepository repo;

    @Inject
    TeamService teamService;

    @Transactional
    public TeamMemberResponse create(TeamMemberRequest request)
            throws TeamNotFoundException, ConflictingTeamMemberException {
        TeamMember teamMember = new TeamMember();
        setTeamMemberFromRequest(teamMember, request);
        repo.persist(teamMember);
        repo.flush();
        return TeamMemberResponse.from(teamMember);
    }

    @Transactional
    public List<TeamMemberResponse> getAll() {
        return repo.findAll().stream().map(TeamMemberResponse::from).toList();
    }

    @Transactional
    public Optional<TeamMemberResponse> getByIdResponse(Long id) {
        Optional<TeamMember> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        TeamMember teamMember = fetched.get();
        return Optional.of(TeamMemberResponse.from(teamMember));
    }

    @Transactional
    public Optional<TeamMemberResponse> update(Long id, TeamMemberRequest request)
            throws TeamNotFoundException, ConflictingTeamMemberException {
        Optional<TeamMember> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        TeamMember teamMember = fetched.get();
        setTeamMemberFromRequest(teamMember, request);
        repo.persist(teamMember);
        repo.flush();
        return Optional.of(TeamMemberResponse.from(teamMember));
    }

    @Transactional
    public Optional<TeamMemberResponse> delete(Long id) {
        Optional<TeamMember> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        TeamMember teamMember = fetched.get();
        repo.delete(teamMember);
        repo.flush();
        return Optional.of(TeamMemberResponse.from(teamMember));
    }

    private void setTeamMemberFromRequest(TeamMember entity, TeamMemberRequest request)
            throws TeamNotFoundException, ConflictingTeamMemberException {
        Optional<Team> teamOptional = teamService.getById(request.teamId());
        if (teamOptional.isEmpty()) throw new TeamNotFoundException(request.teamId());

        Optional<TeamMember> conflicting = getByCtwId(request.ctwId());
        if (conflicting.isPresent()) throw new ConflictingTeamMemberException(request.ctwId());

        Team team = teamOptional.get();

        entity.setTeam(team);
        entity.setCtwId(request.ctwId());
        entity.setName(request.name());
    }

    @Transactional
    public Optional<TeamMember> getById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    @Transactional
    public Optional<TeamMember> getByCtwId(String ctwId) {
        return Optional.ofNullable(repo.findByCtwId(ctwId));
    }
}
