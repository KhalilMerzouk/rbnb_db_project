
---------------------------------------------
--query 10

select total_listing.NEIGHBORHOOD
from 

(select count(*) as total, LOC2.NEIGHBORHOOD      --total number of listings per neighborhood
from LISTING_LOCATION LOC2, LISTING L3
where LOC2.LISTING_ID = L3.LISTING_ID and L3.CITY = 'Madrid' group by LOC2.NEIGHBORHOOD) total_listing 

,

(select count(*) as occupied_listings, LOC.NEIGHBORHOOD
from LISTING_LOCATION LOC, LISTING L2
where L2.LISTING_ID = LOC.LISTING_ID and L2.CITY = 'Madrid' and L2.LISTING_ID in (

  select distinct L1.LISTING_ID      --listing in madrid that were taken in 2019 
  from LISTING L1, CALENDAR CAL
  where CAL.CALENDAR_DATE >= date '2019-01-01' and CAL.AVAILABLE = 'f' and L1.LISTING_ID = CAL.LISTING_ID 
  and L1.LISTING_ID in (

    select L.LISTING_ID   --listing in madrid whose host is member since '2017-06-01' 
    from LISTING L, HOST H
    where L.CITY = 'Madrid' and L.HOST_ID = H.HOST_ID and H.HOST_SINCE >= date '2017-06-01' 
  )

) group by LOC.NEIGHBORHOOD) filtered_listing

where total_listing.NEIGHBORHOOD = filtered_listing.NEIGHBORHOOD and (filtered_listing.occupied_listings / total_listing.total) >= 0.5;


-----------------------------------------------------------------------------
--query 11

select filtered.COUNTRY
from 

(select count(*) as available, L.COUNTRY
from LISTING L
where L.LISTING_ID in (
 select distinct L1.LISTING_ID     
  from LISTING L1, CALENDAR CAL
  where CAL.CALENDAR_DATE >= date '2018-01-01' and CAL.CALENDAR_DATE < date '2019-01-01' and CAL.AVAILABLE = 't' and L1.LISTING_ID = CAL.LISTING_ID
  
) group by L.COUNTRY) filtered
 
,

(select count(*) total_listing, L2.COUNTRY
from LISTING L2
group by L2.COUNTRY) total

where filtered.COUNTRY = total.COUNTRY and (filtered.available/ total.total_listing) >= 0.2;

------------------------------------------------------------
--query 12


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


------------------------------------------------------------

