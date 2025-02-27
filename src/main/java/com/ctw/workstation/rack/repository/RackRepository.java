package com.ctw.workstation.rack.repository;

import com.ctw.workstation.rack.entity.Rack;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RackRepository implements PanacheRepository<Rack> {
    public Rack findBySerialNumber(String serialNumber) {
        return find("serialNumber", serialNumber).firstResult();
    }
}
