-- Generated by Oracle SQL Developer Data Modeler 18.4.0.339.1532
--   at:        2019-04-03 14:16:00 CEST
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g



CREATE TABLE amenities (
    amenity_id     INTEGER NOT NULL,
    amenity_name   VARCHAR2(4000) NOT NULL
);

ALTER TABLE amenities ADD CONSTRAINT amenities_pk PRIMARY KEY ( amenity_id );

CREATE TABLE calendar (
    listing_id   INTEGER NOT NULL,
    "DATE"       DATE NOT NULL,
    available    CHAR(1) NOT NULL,
    price        NUMBER
);

ALTER TABLE calendar ADD CONSTRAINT calendar_pk PRIMARY KEY ( listing_id,
                                                              "DATE" );

CREATE TABLE costs_details (
    listing_id         INTEGER NOT NULL,
    price              NUMBER,
    weekly_price       NUMBER,
    monthly_price      NUMBER,
    security_deposit   NUMBER,
    cleaning_fee       NUMBER,
    guests_included    VARCHAR2(4000),
    extra_people       NUMBER
);

ALTER TABLE costs_details ADD CONSTRAINT costs_details_pk PRIMARY KEY ( listing_id );

CREATE TABLE host (
    host_id              INTEGER NOT NULL,
    host_url             VARCHAR2(4000) NOT NULL,
    host_name            VARCHAR2(4000) NOT NULL,
    host_since           DATE,
    host_thumbnail_url   VARCHAR2(4000)
);

ALTER TABLE host ADD CONSTRAINT host_pk PRIMARY KEY ( host_id );

CREATE TABLE host_details (
    host_id              INTEGER NOT NULL,
    host_about           VARCHAR2(4000),
    host_response_time   VARCHAR2(4000),
    host_response_rate   VARCHAR2(4000),
    host_picture_url     VARCHAR2(4000),
    host_neighborhood    VARCHAR2(4000),
    host_verifications   VARCHAR2(4000)
);

ALTER TABLE host_details ADD CONSTRAINT host_details_pk PRIMARY KEY ( host_id );

CREATE TABLE host_verifications (
    host_id           INTEGER NOT NULL,
    verification_id   INTEGER NOT NULL
);

ALTER TABLE host_verifications ADD CONSTRAINT host_verifications_pk PRIMARY KEY ( host_id,
                                                                                  verification_id );

CREATE TABLE listing (
    listing_id    INTEGER NOT NULL,
    listing_url   VARCHAR2(4000) NOT NULL,
    name          VARCHAR2(4000) NOT NULL,
    summary       VARCHAR2(4000),
    picture_url   VARCHAR2(4000),
    country       VARCHAR2(4000),
    city          VARCHAR2(4000),
    host_id       INTEGER NOT NULL
);

ALTER TABLE listing ADD CONSTRAINT listing_pk PRIMARY KEY ( listing_id );

CREATE TABLE listing_amenities (
    listing_id   INTEGER NOT NULL,
    amenity_id   INTEGER NOT NULL
);

ALTER TABLE listing_amenities ADD CONSTRAINT listing_amenities_pk PRIMARY KEY ( listing_id,
                                                                                amenity_id );

CREATE TABLE listing_details (
    listing_id                      INTEGER
        CONSTRAINT nnc_listing_details_listing_id NOT NULL,
    space                           VARCHAR2(4000),
    description                     VARCHAR2(4000),
    neighborhood_overview           VARCHAR2(4000),
    notes                           VARCHAR2(4000),
    transit                         VARCHAR2(4000),
    "ACCESS"                        VARCHAR2(4000),
    interaction                     VARCHAR2(4000),
    house_rules                     VARCHAR2(4000),
    minimum_nights                  INTEGER,
    maximum_nights                  INTEGER,
    is_business_travel_ready        CHAR(1),
    cancellation_policy             VARCHAR2(4000),
    require_guest_profile_picture   CHAR(1),
    require_guest_phone_verif       CHAR(1)
);

ALTER TABLE listing_details ADD CONSTRAINT listing_details_pk PRIMARY KEY ( listing_id );

CREATE TABLE listing_location (
    listing_id     INTEGER NOT NULL,
    neighborhood   VARCHAR2(4000),
    country_code   VARCHAR2(2),
    latitude       FLOAT,
    longitude      FLOAT
);

ALTER TABLE listing_location ADD CONSTRAINT listing_location_pk PRIMARY KEY ( listing_id );

CREATE TABLE material_description (
    listing_id      INTEGER NOT NULL,
    property_type   VARCHAR2(4000),
    room_type       VARCHAR2(4000),
    accomodates     INTEGER,
    bathrooms       INTEGER,
    bedrooms        INTEGER,
    beds            INTEGER,
    bed_type        VARCHAR2(4000),
    square_feet     INTEGER
);

ALTER TABLE material_description ADD CONSTRAINT material_description_pk PRIMARY KEY ( listing_id );

CREATE TABLE reviewer (
    reviewer_id     INTEGER NOT NULL,
    reviewer_name   VARCHAR2(4000) NOT NULL
);

ALTER TABLE reviewer ADD CONSTRAINT reviewer_pk PRIMARY KEY ( reviewer_id );

CREATE TABLE reviews (
    review_id     INTEGER NOT NULL,
    listing_id    INTEGER NOT NULL,
    reviewer_id   INTEGER NOT NULL,
    comments      VARCHAR2(4000) NOT NULL,
    "DATE"        DATE NOT NULL
);

ALTER TABLE reviews ADD CONSTRAINT reviews_pk PRIMARY KEY ( review_id );

CREATE TABLE reviews_scores (
    listing_id                    INTEGER NOT NULL,
    review_scores_rating          INTEGER,
    review_scores_accuracy        INTEGER,
    review_scores_cleanliness     INTEGER,
    review_scores_checkin         INTEGER,
    review_scores_communication   INTEGER,
    review_scores_location        INTEGER,
    review_scores_value           INTEGER
);

ALTER TABLE reviews_scores ADD CONSTRAINT reviews_scores_pk PRIMARY KEY ( listing_id );

CREATE TABLE verifications (
    verification_id     INTEGER NOT NULL,
    verification_name   VARCHAR2(4000) NOT NULL
);

ALTER TABLE verifications ADD CONSTRAINT verifications_pk PRIMARY KEY ( verification_id );

ALTER TABLE calendar
    ADD CONSTRAINT calendar_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE costs_details
    ADD CONSTRAINT costs_details_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE host_details
    ADD CONSTRAINT host_details_host_fk FOREIGN KEY ( host_id )
        REFERENCES host ( host_id )
            ON DELETE CASCADE;

ALTER TABLE host_verifications
    ADD CONSTRAINT host_verif_host_details_fk FOREIGN KEY ( host_id )
        REFERENCES host_details ( host_id )
            ON DELETE CASCADE;

ALTER TABLE host_verifications
    ADD CONSTRAINT host_verif_verif_fk FOREIGN KEY ( verification_id )
        REFERENCES verifications ( verification_id )
            ON DELETE CASCADE;

ALTER TABLE listing_amenities
    ADD CONSTRAINT listing_amenities_amenities_fk FOREIGN KEY ( amenity_id )
        REFERENCES amenities ( amenity_id )
            ON DELETE CASCADE;

ALTER TABLE listing_amenities
    ADD CONSTRAINT listing_amenities_mat_descr_fk FOREIGN KEY ( listing_id )
        REFERENCES material_description ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE listing_details
    ADD CONSTRAINT listing_details_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE listing
    ADD CONSTRAINT listing_host_fk FOREIGN KEY ( host_id )
        REFERENCES host ( host_id )
            ON DELETE CASCADE;

ALTER TABLE listing_location
    ADD CONSTRAINT listing_location_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE material_description
    ADD CONSTRAINT material_descr_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE reviews
    ADD CONSTRAINT reviews_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

ALTER TABLE reviews
    ADD CONSTRAINT reviews_reviewer_fk FOREIGN KEY ( reviewer_id )
        REFERENCES reviewer ( reviewer_id );

ALTER TABLE reviews_scores
    ADD CONSTRAINT reviews_scores_listing_fk FOREIGN KEY ( listing_id )
        REFERENCES listing ( listing_id )
            ON DELETE CASCADE;

CREATE SEQUENCE amenities_amenity_id_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER amenities_amenity_id_trg BEFORE
    INSERT ON amenities
    FOR EACH ROW
    WHEN ( new.amenity_id IS NULL )
BEGIN
    :new.amenity_id := amenities_amenity_id_seq.nextval;
END;
/

CREATE SEQUENCE verifications_verification_id START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER verifications_verification_id BEFORE
    INSERT ON verifications
    FOR EACH ROW
    WHEN ( new.verification_id IS NULL )
BEGIN
    :new.verification_id := verifications_verification_id.nextval;
END;
/



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            15
-- CREATE INDEX                             0
-- ALTER TABLE                             29
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           2
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          2
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
