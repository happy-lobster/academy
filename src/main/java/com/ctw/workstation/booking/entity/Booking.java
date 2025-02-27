package com.ctw.workstation.booking.entity;

import com.ctw.workstation.rack.entity.Rack;
import com.ctw.workstation.teammember.entity.TeamMember;
import com.ctw.workstation.utils.TimeInterval;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_BOOKING")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookingIdGenerator")
    @SequenceGenerator(name = "bookingIdGenerator", sequenceName = "SEQ_BOOKING_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id")
    private Rack rack;

    @OneToOne(fetch = FetchType.LAZY)
    private TeamMember requester;

    @Column(name = "book_from")
    private LocalDateTime from;

    @Column(name = "book_to")
    private LocalDateTime to;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Booking() {}

    public Long getId() {
        return id;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public TeamMember getRequester() {
        return requester;
    }

    public void setRequester(TeamMember requester) {
        this.requester = requester;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean overlaps(LocalDateTime from, LocalDateTime to) {
        TimeInterval timeInterval = new TimeInterval(from, to);
        TimeInterval thisTimeInterval = new TimeInterval(this.from, this.to);
        return thisTimeInterval.overlaps(timeInterval);
    }
}