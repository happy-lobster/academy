package com.ctw.workstation.team.entity;

import com.ctw.workstation.rack.entity.Rack;
import com.ctw.workstation.team.dto.TeamRequest;
import com.ctw.workstation.teammember.entity.TeamMember;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_TEAM")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teamIdGenerator")
    @SequenceGenerator(name = "teamIdGenerator", sequenceName = "SEQ_TEAM_ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "product")
    private String product;

    @Column(name = "default_location")
    private String defaultLocation;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private final List<Rack> racks = new ArrayList<Rack>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private final List<TeamMember> members = new ArrayList<TeamMember>();

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Team() {}

    public Team(String name, String product, String defaultLocation) {
        this.name = name;
        this.product = product;
        this.defaultLocation = defaultLocation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(String location) {
        this.defaultLocation = location;
    }

    public List<Rack> getRacks() {
        return racks;
    }

    public List<TeamMember> getMembers() {
        return members;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Team other) {
            if (this.id != null) {
                return this.id.equals(other.getId());
            }
        }
        return false;
    }

    public static Team from(TeamRequest request) {
        return new Team(request.name(), request.product(), request.defaultLocation());
    }
}
