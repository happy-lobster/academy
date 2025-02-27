package com.ctw.workstation.team.repository;

import com.ctw.workstation.team.entity.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamRepository implements PanacheRepository<Team> {
    public Team findByName(String name) {
        return find("name", name).firstResult();
    }
}
