package com.ctw.workstation.teammember.resource;

import com.ctw.workstation.team.dto.TeamPartialResponse;
import com.ctw.workstation.team.entity.Team;
import com.ctw.workstation.team.repository.TeamRepository;
import com.ctw.workstation.teammember.dto.TeamMemberRequest;
import com.ctw.workstation.teammember.dto.TeamMemberResponse;
import com.ctw.workstation.teammember.entity.TeamMember;
import com.ctw.workstation.teammember.repository.TeamMemberRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@TestHTTPEndpoint(TeamMemberResource.class)
class TeamMemberResourceTest {
    @Inject
    TeamRepository teamRepository;

    @Inject
    TeamMemberRepository teamMemberRepository;

    private Team team;
    private Team newTeam;
    private TeamMember existingTeamMember;

    @BeforeEach
    @Transactional
    void setup() {
        Team team = new Team();
        team.setName("Team");
        team.setProduct("Product");
        team.setDefaultLocation("Porto");
        teamRepository.persist(team);
        teamRepository.flush();
        this.team = team;

        Team newTeam = new Team();
        newTeam.setName("New Team");
        newTeam.setProduct("New Product");
        newTeam.setDefaultLocation("Lisboa");
        teamRepository.persist(newTeam);
        teamRepository.flush();
        this.newTeam = newTeam;

        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(team);
        teamMember.setCtwId("CTW00001");
        teamMember.setName("João");
        teamMemberRepository.persist(teamMember);
        teamMemberRepository.flush();
        this.existingTeamMember = teamMember;
    }

    @AfterEach
    @Transactional
    void teardown() {
        teamMemberRepository.deleteAll();
        teamMemberRepository.flush();

        teamRepository.deleteAll();
        teamRepository.flush();
    }

    @Test
    @DisplayName("Create TeamMember")
    void createValidTeamMember() {
        String ctwId = "CTW00123";
        String name = "Pedro";
        TeamMemberRequest request = new TeamMemberRequest(this.team.getId(), name, ctwId);
        TeamMemberResponse response = given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post()
            .then()
                .statusCode(RestResponse.StatusCode.CREATED)
                .extract()
                .as(TeamMemberResponse.class);
        assertThat(response)
            .extracting("ctwId", "name", "team")
            .containsExactly(ctwId, name, TeamPartialResponse.from(this.team));

        assertThat(this.teamMemberRepository.count()).isEqualTo(2);
    }

    static Stream<Arguments> invalidTeamMemberData() {
        return Stream.of(
            Arguments.of("CTW00004", ""),
            Arguments.of("CTW00004", null),
            Arguments.of("", "Álvaro"),
            Arguments.of(null, "Álvaro")
        );
    }

    @ParameterizedTest
    @MethodSource(value = {"invalidTeamMemberData"})
    @DisplayName("Create invalid TeamMember")
    void createInvalidTeamMember(String ctwId, String name) {
        TeamMemberRequest request = new TeamMemberRequest(this.team.getId(), ctwId, name);
        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post()
            .then()
                .statusCode(RestResponse.StatusCode.BAD_REQUEST);

        assertThat(this.teamMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Create TeamMember with conflicting ctwId")
    void createTeamMemberWithConflictCtwId() {
        TeamMemberRequest request = new TeamMemberRequest(this.team.getId(), "Pedro", this.existingTeamMember.getCtwId());
        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post()
            .then()
                .statusCode(RestResponse.StatusCode.CONFLICT);

        assertThat(this.teamMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Create TeamMember with non existing Team")
    void createTeamMemberWithNonExistingTeam() {
        TeamMemberRequest request = new TeamMemberRequest(32875L, "CTW00312", "Pedro");
        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post()
                .then()
                .statusCode(RestResponse.StatusCode.BAD_REQUEST);

        assertThat(this.teamMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Fetch all TeamMembers")
    void fetchAllTeamMembers() {
        TeamMemberResponse[] response = given()
            .when()
                .get()
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamMemberResponse[].class);
        assertThat(response.length).isEqualTo(1);
    }

    @Test
    @DisplayName("Fetch TeamMember by id")
    void fetchTeamMemberById() {
        TeamMemberResponse response = given()
            .when()
                .get(this.existingTeamMember.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamMemberResponse.class);
        assertThat(response).isEqualTo(TeamMemberResponse.from(this.existingTeamMember));
    }

    @Test
    @DisplayName("Fetch TeamMember by non existing id")
    void fetchTeamMemberByNonExistingId() {
        given()
            .when()
                .get("{0}", this.existingTeamMember.getId() + 1)
            .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
    }

    @Test
    @DisplayName("Update valid TeamMember")
    void updateValidTeamMember() {
        String newCtwId = "CTW00987";
        String newName = "Alfredo";
        TeamMemberRequest request = new TeamMemberRequest(this.newTeam.getId(), newName, newCtwId);
        TeamMemberResponse response = given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put(this.existingTeamMember.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamMemberResponse.class);
        assertThat(response)
            .extracting("ctwId", "name", "team")
            .containsExactly(newCtwId, newName, TeamPartialResponse.from(this.newTeam));

        TeamMember teamMember = this.teamMemberRepository.findById(this.existingTeamMember.getId());
        assertThat(teamMember.getCtwId()).isEqualTo(newCtwId);
        assertThat(teamMember.getName()).isEqualTo(newName);
        assertThat(teamMember.getTeam()).isEqualTo(newTeam);
    }

    @ParameterizedTest
    @MethodSource(value = {"invalidTeamMemberData"})
    @DisplayName("Update TeamMember with invalid fields")
    void updateTeamMemberWithInvalidFields(String ctwId, String name) {
        TeamMemberRequest request = new TeamMemberRequest(this.newTeam.getId(), ctwId, name);
        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put(this.existingTeamMember.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.BAD_REQUEST);

        assertThat(this.existingTeamMember.getCtwId()).isNotEqualTo(ctwId);
        assertThat(this.existingTeamMember.getName()).isNotEqualTo(name);
        assertThat(this.existingTeamMember.getTeam()).isNotEqualTo(newTeam);
    }

    @Test
    @DisplayName("Update TeamMember with non existing Team")
    void updateTeamMemberWithNonExistingTeam() {
        Long newTeamId = 1248L;
        String newCtwId = "CTW00987";
        String newName = "Alfredo";
        TeamMemberRequest request = new TeamMemberRequest(newTeamId, newCtwId, newName);
        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put(this.existingTeamMember.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.BAD_REQUEST);

        assertThat(this.existingTeamMember.getTeam().getId()).isNotEqualTo(newTeamId);
        assertThat(this.existingTeamMember.getCtwId()).isNotEqualTo(newCtwId);
        assertThat(this.existingTeamMember.getName()).isNotEqualTo(newName);
    }

    @Test
    @DisplayName("Update non existing TeamMember")
    void updateNonExistingTeamMember() {
        String newCtwId = "CTW00987";
        String newName = "Alfredo";
        TeamMemberRequest request = new TeamMemberRequest(this.newTeam.getId(), newCtwId, newName);
        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put("{0}", this.existingTeamMember.getId() + 1)
            .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);

        assertThat(this.existingTeamMember.getCtwId()).isNotEqualTo(newCtwId);
        assertThat(this.existingTeamMember.getName()).isNotEqualTo(newName);
        assertThat(this.existingTeamMember.getTeam()).isNotEqualTo(newTeam);
    }

    @Test
    @DisplayName("Delete TeamMember")
    void deleteTeamMember() {
        TeamMemberResponse response = given()
            .when()
                .delete(this.existingTeamMember.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamMemberResponse.class);
        assertThat(response).isEqualTo(TeamMemberResponse.from(this.existingTeamMember));

        assertThat(this.teamMemberRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Delete non existing TeamMember")
    void deleteNonExistingTeamMember() {
        given()
            .when()
                .delete("{0}", this.existingTeamMember.getId() + 1)
            .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
        assertThat(this.teamMemberRepository.count()).isEqualTo(1);
    }
}