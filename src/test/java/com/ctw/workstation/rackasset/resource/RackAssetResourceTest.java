package com.ctw.workstation.rackasset.resource;

import com.ctw.workstation.rack.repository.RackRepository;
import com.ctw.workstation.rackasset.repository.RackAssetRepository;
import com.ctw.workstation.team.repository.TeamRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(RackAssetResource.class)
class RackAssetResourceTest {
    @Inject
    TeamRepository teamRepository;

    @Inject
    RackRepository rackRepository;

    @Inject
    RackAssetRepository rackAssetRepository;

    @BeforeEach
    void setup() {}

    @AfterEach
    void teardown() {}

    @Test
    @DisplayName("Create valid RackAsset")
    void testCreateValidRackAsset() {}

    @Test
    @DisplayName("Create invalid RackAsset")
    void testCreateInvalidRackAsset() {}

    @Test
    @DisplayName("Create RackAsset for non existing Rack")
    void testCreateRackAssetNonExistingRack() {}

    @Test
    @DisplayName("Fetch all RackAssets")
    void testFetchAllRackAssets() {}

    @Test
    @DisplayName("Fetch existing RackAsset by id")
    void testFetchExistingRackAssetById() {}

    @Test
    @DisplayName("Fetch not existing RackAsset by id")
    void testFetchNotExistingRackAssetById() {}

    @Test
    @DisplayName("Update RackAsset with valid fields")
    void testUpdateRackAssetWithValidFields() {}

    @Test
    @DisplayName("Update RackAsset with invalid fields")
    void testUpdateRackAssetWithInvalidFields() {}

    @Test
    @DisplayName("Update not existing RackAsset")
    void testUpdateRackAssetNonExistingRackAsset() {}

    @Test
    @DisplayName("Update RackAsset with not existing Rack")
    void testUpdateRackAssetWithNotExistingRack() {}

    @Test
    @DisplayName("Delete RackAsset")
    void testDeleteRackAsset() {}

    @Test
    @DisplayName("Delete not existing RackAsset")
    void testDeleteRackAssetNonExistingRackAsset() {}
}