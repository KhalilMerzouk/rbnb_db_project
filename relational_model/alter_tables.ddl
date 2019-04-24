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
