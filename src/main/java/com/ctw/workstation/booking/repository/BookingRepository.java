package com.ctw.workstation.booking.repository;

import com.ctw.workstation.booking.entity.Booking;
import com.ctw.workstation.rack.entity.Rack;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {
    public List<Booking> getByRack(Rack rack) {
        return find("rack", rack).list();
    }
}
