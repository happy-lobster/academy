package com.ctw.workstation.team.resource;

import com.ctw.workstation.team.dto.TeamRequest;
import com.ctw.workstation.team.dto.TeamResponse;
import com.ctw.workstation.team.entity.Team;
import com.ctw.workstation.team.repository.TeamRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestHTTPEndpoint(TeamResource.class)
class TeamResourceTest {
    @Inject
    TeamRepository repository;

    private Team existingTeam;

    @BeforeEach
    @Transactional
    void setup() {
        this.existingTeam = new Team();
        this.existingTeam.setName("Basic Team");
        this.existingTeam.setProduct("Basic Product");
        this.existingTeam.setDefaultLocation("Porto");
        this.repository.persist(this.existingTeam);
        this.repository.flush();
    }

    @AfterEach
    @Transactional
    void teardown() {
        this.repository.deleteAll();
        this.repository.flush();
    }

    @Test
    @DisplayName("Create a valid team")
    void createValidTeam() {
        String name = "New Team";
        String product = "New Product";
        String defaultLocation = "Lisbon";
        TeamRequest request = new TeamRequest(name, product, defaultLocation);
        TeamResponse response = given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .post()
            .then()
                .statusCode(RestResponse.StatusCode.CREATED)
                .extract()
                .as(TeamResponse.class);
        assertThat(response)
            .extracting("name", "product", "defaultLocation", "racks", "members")
            .containsExactly(name, product, defaultLocation, Collections.emptyList(), Collections.emptyList());
        assertThat(this.repository.count()).isEqualTo(2);
    }

    public static Stream<Arguments> createInvalidTeamData() {
        return Stream.of(
            Arguments.of("Super Team", "Super Product", ""),
            Arguments.of("Super Team", "", "Porto"),
            Arguments.of("", "Super Product", "Porto"),
            Arguments.of("Super Team", "Super Product", null),
            Arguments.of("Super Team", null, "Porto"),
            Arguments.of(null, "Super Product", "Porto")
        );
    }

    @ParameterizedTest
    @MethodSource(value = {"createInvalidTeamData"})
    @DisplayName("Create a team with invalid request data")
    void createInvalidTeam(String name, String product, String defaultLocation) {
        TeamRequest teamRequest = new TeamRequest(name, product, defaultLocation);

        given()
                .header("Content-Type", "application/json")
                .body(teamRequest)
            .when()
                .post()
            .then()
                .statusCode(RestResponse.StatusCode.BAD_REQUEST);
        assertThat(this.repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Create conflicting team")
    void createConflictingTeam() {
        TeamRequest teamRequest = new TeamRequest("Basic Team", "Super Product", "Porto");

        given()
                .header("Content-Type", "application/json")
                .body(teamRequest)
            .when()
                .post()
            .then()
                .statusCode(RestResponse.StatusCode.CONFLICT);

        assertThat(this.repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Fetch all teams")
    void getAll() {
        TeamResponse[] response = given()
                .header("Content-Type", "application/json")
            .when()
                .get()
            .then()
                .statusCode(200)
                .extract()
                .as(TeamResponse[].class);
        assertThat(response.length).isEqualTo(1);
        assertThat(response[0]).isEqualTo(TeamResponse.from(this.existingTeam));
    }

    @Test
    @DisplayName("Fetch team by existing id")
    void getTeamById() {
        TeamResponse response = given()
            .when()
                .get(this.existingTeam.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamResponse.class);
        assertThat(response)
            .isEqualTo(TeamResponse.from(this.existingTeam));
    }

    @Test
    @DisplayName("Fetch team by non existent id")
    void getTeamByInvalidId() {
        given()
            .when()
                .get("{0}", this.existingTeam.getId() + 1)
            .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);
    }

    @Test
    @DisplayName("Update team")
    void updateTeam() {
        String newName = "Super Team";
        String newProduct = "Super Product";
        String newDefaultLocation = "Braga";
        TeamRequest teamRequest = new TeamRequest(newName, newProduct, newDefaultLocation);

        TeamResponse response = given()
                .header("Content-Type", "application/json")
                .body(teamRequest)
            .when()
                .put(this.existingTeam.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamResponse.class);
        assertThat(response)
                .extracting("id", "name", "product", "defaultLocation", "racks", "members")
                .containsExactly(this.existingTeam.getId(), newName, newProduct, newDefaultLocation, Collections.emptyList(), Collections.emptyList());

        Team team = this.repository.findById(this.existingTeam.getId());

        assertThat(team.getName()).isEqualTo(newName);
        assertThat(team.getProduct()).isEqualTo(newProduct);
        assertThat(team.getDefaultLocation()).isEqualTo(newDefaultLocation);
    }

    public static Stream<Arguments> updateTeamInvalidData() {
        return Stream.of(
            Arguments.of("Super Team", "Super Product", null),
            Arguments.of("Super Team", "Super Product", ""),
            Arguments.of("Super Team", null, "Braga"),
            Arguments.of("Super Team", "", "Braga"),
            Arguments.of(null, "Super Product", "Braga"),
            Arguments.of("", "Super Product", "Braga")
        );
    }

    @ParameterizedTest
    @MethodSource(value = {"updateTeamInvalidData"})
    @DisplayName("Update team with invalid update request")
    void updateTeamInvalid(String name, String product, String defaultLocation) {
        TeamRequest request = new TeamRequest(name, product, defaultLocation);

        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put(this.existingTeam.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.BAD_REQUEST);

        Team team = this.repository.findById(this.existingTeam.getId());
        assertThat(team.getName()).isNotEqualTo(name);
        assertThat(team.getProduct()).isNotEqualTo(product);
        assertThat(team.getDefaultLocation()).isNotEqualTo(defaultLocation);
    }

    @Test
    @DisplayName("Update team with not existing id")
    void updateNotExistingTeam() {
        String newName = "Super Team";
        String newProduct = "Super Product";
        String newDefaultLocation = "Braga";
        TeamRequest request = new TeamRequest(newName, newProduct, newDefaultLocation);

        given()
                .header("Content-Type", "application/json")
                .body(request)
            .when()
                .put("{0}", this.existingTeam.getId() + 1)
            .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);

        Team team = this.repository.findById(this.existingTeam.getId());
        assertThat(team.getName()).isNotEqualTo(newName);
        assertThat(team.getProduct()).isNotEqualTo(newProduct);
        assertThat(team.getDefaultLocation()).isNotEqualTo(newDefaultLocation);
    }

    @Test
    @DisplayName("Delete team")
    void deleteTeam() {
        TeamResponse response = given()
            .when()
                .delete(this.existingTeam.getId().toString())
            .then()
                .statusCode(RestResponse.StatusCode.OK)
                .extract()
                .as(TeamResponse.class);
        assertThat(response)
                .isEqualTo(TeamResponse.from(this.existingTeam));

        assertThat(this.repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Delete team with non existing id")
    void deleteNotExistingTeam() {
        given()
            .when()
                .delete("{0}", this.existingTeam.getId() + 1)
            .then()
                .statusCode(RestResponse.StatusCode.NOT_FOUND);

        assertThat(this.repository.count()).isEqualTo(1);
    }
}