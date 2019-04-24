CREATE TABLE amenities (
    amenity_id     INTEGER NOT NULL,
    amenity_name   VARCHAR2(100) NOT NULL
);

ALTER TABLE amenities ADD CONSTRAINT amenities_pk PRIMARY KEY ( amenity_id );

CREATE TABLE calendar (
    listing_id      INTEGER NOT NULL,
    calendar_date   DATE NOT NULL,
    available       CHAR(1) NOT NULL,
    price           NUMBER
);

ALTER TABLE calendar ADD CONSTRAINT calendar_pk PRIMARY KEY ( listing_id,
                                                              calendar_date );

CREATE TABLE costs_details (
    listing_id         INTEGER NOT NULL,
    price              NUMBER,
    weekly_price       NUMBER,
    monthly_price      NUMBER,
    security_deposit   NUMBER,
    cleaning_fee       NUMBER,
    guests_included    INTEGER,
    extra_people       NUMBER
);

ALTER TABLE costs_details ADD CONSTRAINT costs_details_pk PRIMARY KEY ( listing_id );

CREATE TABLE host (
    host_id              INTEGER NOT NULL,
    host_url             VARCHAR2(60) NOT NULL,
    host_name            VARCHAR2(4000) NOT NULL,
    host_since           DATE,
    host_thumbnail_url   VARCHAR2(4000)
);

ALTER TABLE host ADD CONSTRAINT host_pk PRIMARY KEY ( host_id );

CREATE TABLE host_details (
    host_id              INTEGER NOT NULL,
    host_about           VARCHAR2(4000),
    host_response_time   VARCHAR2(50),
    host_response_rate   INTEGER,
    host_picture_url     VARCHAR2(4000),
    host_neighborhood    VARCHAR2(4000)
);

ALTER TABLE host_details ADD CONSTRAINT host_details_pk PRIMARY KEY ( host_id );

CREATE TABLE host_verifications (
    host_id           INTEGER NOT NULL,
    verification_id   INTEGER NOT NULL
);

ALTER TABLE host_verifications ADD CONSTRAINT host_verifications_pk PRIMARY KEY ( host_id,
                                                                                  verification_id );

CREATE TABLE listing (
    listing_id        INTEGER NOT NULL,
    listing_url       VARCHAR2(4000) NOT NULL,
    listing_name      VARCHAR2(4000) NOT NULL,
    listing_summary   VARCHAR2(4000),
    picture_url       VARCHAR2(4000),
    country           VARCHAR2(20),
    city              VARCHAR2(20),
    host_id           INTEGER NOT NULL
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
    listing_space                   VARCHAR2(4000),
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
    cancellation_policy             VARCHAR2(30),
    require_guest_profile_picture   CHAR(1),
    require_guest_phone_verif       CHAR(1)
);

ALTER TABLE listing_details ADD CONSTRAINT listing_details_pk PRIMARY KEY ( listing_id );

CREATE TABLE listing_location (
    listing_id     INTEGER NOT NULL,
    neighborhood   VARCHAR2(100),
    country_code   VARCHAR2(2),
    latitude       FLOAT,
    longitude      FLOAT
);

ALTER TABLE listing_location ADD CONSTRAINT listing_location_pk PRIMARY KEY ( listing_id );

CREATE TABLE material_description (
    listing_id      INTEGER NOT NULL,
    property_type   VARCHAR2(30),
    room_type       VARCHAR2(20),
    accomodates     INTEGER,
    bathrooms       INTEGER,
    bedrooms        INTEGER,
    beds            INTEGER,
    bed_type        VARCHAR2(20),
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
    review_date   DATE NOT NULL
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
    verification_name   VARCHAR2(100) NOT NULL
);

ALTER TABLE verifications ADD CONSTRAINT verifications_pk PRIMARY KEY ( verification_id );

