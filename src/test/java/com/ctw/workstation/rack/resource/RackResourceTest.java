package com.ctw.workstation.rack.resource;

import com.ctw.workstation.rack.repository.RackRepository;
import com.ctw.workstation.team.repository.TeamRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(RackResource.class)
class RackResourceTest {
    @Inject
    RackRepository rackRepository;

    @Inject
    TeamRepository teamRepository;

    @BeforeEach
    void setup() {}

    @AfterEach
    void teardown() {}

    @Test
    @DisplayName("Create valid Rack")
    void createValidRack() {}

    @Test
    @DisplayName("Create invalid Rack")
    void createInvalidRack() {}

    @Test
    @DisplayName("Create Rack with non existing Team")
    void createRackWithNonExistingTeam() {}

    @Test
    @DisplayName("Create Rack with conflicting serialNumber")
    void createRackWithConflictingSerialNumber() {}

    @Test
    @DisplayName("Fetch all Racks")
    void fetchAllRacks() {}

    @Test
    @DisplayName("Fetch existing Rack by id")
    void fetchExistingRackById() {}

    @Test
    @DisplayName("Fetch non existing Rack by id")
    void fetchNonExistingRackById() {}

    @Test
    @DisplayName("Update Rack with valid fields")
    void updateRackWithValidFields() {}

    @Test
    @DisplayName("Update Rack with invalid fields")
    void updateRackWithInvalidFields() {}

    @Test
    @DisplayName("Update Rack with non existing Team")
    void updateRackWithNonExistingTeam() {}

    @Test
    @DisplayName("Update non existing Rack")
    void updateNonExistingRack() {}

    @Test
    @DisplayName("Delete existing Rack")
    void deleteExistingRack() {}

    @Test
    @DisplayName("Delete non existing Rack")
    void deleteNonExistingRack() {}
}