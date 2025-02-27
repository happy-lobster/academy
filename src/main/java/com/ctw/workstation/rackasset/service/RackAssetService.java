package com.ctw.workstation.rackasset.service;

import com.ctw.workstation.exception.RackNotFoundException;
import com.ctw.workstation.rack.entity.Rack;
import com.ctw.workstation.rack.service.RackService;
import com.ctw.workstation.rackasset.dto.RackAssetRequest;
import com.ctw.workstation.rackasset.dto.RackAssetResponse;
import com.ctw.workstation.rackasset.entity.RackAsset;
import com.ctw.workstation.rackasset.repository.RackAssetRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RackAssetService {
    @Inject
    RackAssetRepository repo;

    @Inject
    RackService rackService;

    @Transactional
    public RackAssetResponse create(RackAssetRequest request) throws RackNotFoundException {
        RackAsset entity = new RackAsset();
        setRackAssetFromRequest(entity, request);
        repo.persist(entity);
        repo.flush();
        return RackAssetResponse.from(entity);
    }

    @Transactional
    public List<RackAssetResponse> getAll() {
        return repo.findAll().stream().map(RackAssetResponse::from).toList();
    }

    public Optional<RackAssetResponse> getByIdResponse(Long id) {
        Optional<RackAsset> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        RackAsset entity = fetched.get();
        return Optional.of(RackAssetResponse.from(entity));
    }

    @Transactional
    public Optional<RackAssetResponse> update(Long id, RackAssetRequest request) throws RackNotFoundException {
        Optional<RackAsset> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        RackAsset entity = fetched.get();
        setRackAssetFromRequest(entity, request);
        repo.persist(entity);
        repo.flush();
        return Optional.of(RackAssetResponse.from(entity));
    }

    @Transactional
    public Optional<RackAssetResponse> delete(Long id) {
        Optional<RackAsset> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        RackAsset entity = fetched.get();
        repo.delete(entity);
        repo.flush();
        return Optional.of(RackAssetResponse.from(entity));
    }

    @Transactional
    public Optional<RackAsset> getById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    private void setRackAssetFromRequest(RackAsset entity, RackAssetRequest request) throws RackNotFoundException {
        Optional<Rack> rackOptional = rackService.getById(request.rackId());
        if (rackOptional.isEmpty()) throw new RackNotFoundException(request.rackId());
        Rack rack = rackOptional.get();
        entity.setTag(request.tag());
        entity.setRack(rack);
    }
}
