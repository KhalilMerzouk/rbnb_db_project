

----------------optimized queries--------------------------------------------------------

--optimization for query 12 milestone 3

--without additional indexes : 0.11 seconds
--with additional indexes : 0.05 seconds
--speedup  2.2

create index new_index
on LISTING(CITY);

create index new_index2
on LISTING_LOCATION (NEIGHBORHOOD);


create index new_index3
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




drop index new_index;
drop index new_index2;
drop index new_index3;