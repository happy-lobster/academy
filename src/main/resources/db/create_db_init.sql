CREATE TABLE T_TEAM(
    id BIGINT,
    name VARCHAR(30) NOT NULL,
    product VARCHAR(30) NOT NULL,
    default_location VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    modified_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT PK_T_TEAM_id PRIMARY KEY (id),
    CONSTRAINT T_TEAM_resource_modification_date_consistency CHECK (created_at <= modified_at),
    CONSTRAINT UNIQUE_T_TEAM_name UNIQUE (name)
);

CREATE SEQUENCE IF NOT EXISTS SEQ_TEAM_ID;

CREATE TABLE T_TEAM_MEMBER(
    id BIGINT,
    team_id BIGINT NOT NULL,
    ctw_id VARCHAR(8) NOT NULL,
    name VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK_T_TEAM_MEMBER_id PRIMARY KEY (id),
    CONSTRAINT UNIQUE_T_TEAM_MEMBER_ctw_id UNIQUE (ctw_id),
    CONSTRAINT FK_T_TEAM_MEMBER_team_id FOREIGN KEY (team_id) REFERENCES T_TEAM(id),
    CONSTRAINT T_TEAM_MEMBER_resource_modification_date_consistency CHECK (created_at <= modified_at)
);

CREATE SEQUENCE IF NOT EXISTS SEQ_TEAM_MEMBER_ID;

CREATE TABLE T_RACK(
    id BIGINT,
    serial_number VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    team_id BIGINT NOT NULL,
    default_location VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    modified_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT PK_T_RACK_id PRIMARY KEY (id),
    CONSTRAINT UNIQUE_T_RACK_serial_number UNIQUE(serial_number),
    CONSTRAINT FK_T_RACK_team_id FOREIGN KEY (team_id) REFERENCES T_TEAM(id),
    CONSTRAINT T_RACK_resource_modification_date_consistency CHECK (created_at <= modified_at)
);

CREATE SEQUENCE IF NOT EXISTS SEQ_RACK_ID;

CREATE TABLE T_BOOKING(
    id BIGINT,
    rack_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    book_from TIMESTAMP NOT NULL,
    book_to TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    modified_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT PK_T_BOOKING_id PRIMARY KEY (id),
    CONSTRAINT FK_T_BOOKING_rack_id FOREIGN KEY (rack_id) REFERENCES T_RACK(id),
    CONSTRAINT FK_T_BOOKING_requester_id FOREIGN KEY (requester_id) REFERENCES T_TEAM_MEMBER(id),
    CONSTRAINT T_BOOKING_resource_modification_date_consistency CHECK (created_at <= modified_at),
    CONSTRAINT T_BOOKING_booking_date_consistency CHECK (book_from <= book_from)
);

CREATE SEQUENCE IF NOT EXISTS SEQ_BOOKING_ID;

CREATE TABLE T_RACK_ASSET(
    id BIGINT,
    asset_tag VARCHAR(30) NOT NULL,
    rack_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    modified_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT T_BOOKING_resource_modification_date_consistency CHECK (created_at <= modified_at)
);

CREATE SEQUENCE IF NOT EXISTS SEQ_RACK_ASSET_ID;
