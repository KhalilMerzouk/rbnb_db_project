

----------------optimized queries--------------------------------------------------------

--optimization for query 12 milestone 3

--without additional indexes : 0.12 seconds
--with additional indexes : 0.064 seconds
--speedup  1.875

--why does it work: table access predicates are faster since there is an index on the filtered columns

create index new_index_q12
on LISTING(CITY);


create index new_index2_q12
on LISTING_DETAILS (CANCELLATION_POLICY);



select total.NEIGHBORHOOD
from 

(select count(*) total_list, LOC.NEIGHBORHOOD
from LISTING_LOCATION LOC, LISTING L
where LOC.LISTING_ID = L.LISTING_ID and L.CITY = 'Barcelona' group by LOC.NEIGHBORHOOD) total

,

(
select count(*) strict_count, LOC.NEIGHBORHOOD
from LISTING_LOCATION LOC, LISTING_DETAILS LD, LISTING L2
where LOC.LISTING_ID = LD.LISTING_ID and L2.LISTING_ID = LD.LISTING_ID and L2.CITY = 'Barcelona' and LD.CANCELLATION_POLICY = 'strict_14_with_grace_period' group by LOC.NEIGHBORHOOD

) filtered

where total.NEIGHBORHOOD = filtered.NEIGHBORHOOD and (filtered.strict_count / total.total_list) >= 0.05;




drop index new_index_q12;

drop index new_index2_q12;


------------------------------------------------------
--optimization for query 3


--without additional indexes : 0.044 seconds
--with additional indexes :0.035  seconds
--speedup   1,25

--why does it work: the join predicate is h.host_id = ranked(listing).host _id but since listings don't have an index on the host id => lose performance
-- we only ave to add an index on listing.host_id


create index new_index2_q3
on LISTING (HOST_ID);


select h.HOST_ID, h.HOST_NAME
from
(select HOST_ID, rank() over(order by nbr desc) as rnk
from
(select L.HOST_ID, count(*) as nbr
from LISTING l
group by l.HOST_ID)) ranked,
HOST h 

where h.HOST_ID = ranked.HOST_ID and ranked.rnk = 1;


drop index new_index2_q3;




--------------------------------------------------

--optimization for query 2



--without additional indexes : 0.08 seconds
--with additional indexes : 0.065  seconds
--speedup    1,23

--why does it work: an index on the city facilitates the access to the listing table.


create index new_index_q2    
on LISTING (CITY);




select * from

(select distinct(med_per_ng.NEIGHBORHOOD), REVIEW_SCORES_RATING from

(select distinct(loc.NEIGHBORHOOD), floor((count(*) over(partition by loc.NEIGHBORHOOD)+1)/2) as median_elem_per_ng
from REVIEWS_SCORES rs, LISTING l, LISTING_LOCATION loc
where rs.LISTING_ID = l.LISTING_ID and loc.LISTING_ID = l.LISTING_ID and rs.REVIEW_SCORES_RATING is not null and l.CITY = 'Madrid') med_per_ng,

(select loc.NEIGHBORHOOD, rs.REVIEW_SCORES_RATING, ROW_NUMBER() over(partition by loc.NEIGHBORHOOD order by rs.REVIEW_SCORES_RATING desc) as rnum
from REVIEWS_SCORES rs, LISTING_LOCATION loc, LISTING l
where rs.LISTING_ID = loc.LISTING_ID and l.LISTING_ID = rs.LISTING_ID and l.CITY = 'Madrid' and rs.REVIEW_SCORES_RATING is not null) ranked_by_ng_and_rev

where med_per_ng.NEIGHBORHOOD = ranked_by_ng_and_rev.NEIGHBORHOOD and median_elem_per_ng = rnum
order by REVIEW_SCORES_RATING desc)

where rownum <= 5
;



drop index new_index_q2;
















