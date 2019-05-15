
--queries for milestone 2 (in order)

select AVG(CD.PRICE)
from COSTS_DETAILS CD , LISTING L
where L.LISTING_ID = CD.LISTING_ID AND L.LISTING_ID IN (Select M.LISTING_ID

                                                        from MATERIAL_DESCRIPTION M
                                                        where M.BEDROOMS = 8);
                                                        
   
   
-------------------------------------------------------------------------------------------------------
                                   
select AVG(RS.REVIEW_SCORES_CLEANLINESS)
from REVIEWS_SCORES RS, LISTING L
where RS.LISTING_ID = L.LISTING_ID AND L.LISTING_ID IN (select LA.LISTING_ID
                                                        from AMENITIES AM, LISTING_AMENITIES LA
                                                        where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'TV'
                                                        );
                                                        


-------------------------------------------------------------------------------------------------------

select * 
from HOST H
where H.HOST_ID IN (select L.HOST_ID
                    from LISTING L, CALENDAR CA
                    where L.LISTING_ID = CA.LISTING_ID and CA.AVAILABLE = 't' and CA.CALENDAR_DATE >= '01-MAR-19' and CA.CALENDAR_DATE <= '30-SEP-19'
                    );
 
 
 
------------------------------------------------------------------------------------------------------- 
                   
                    
select COUNT(*)
from LISTING L, HOST H
where L.HOST_ID = H.HOST_ID and H.HOST_ID IN (Select H1.HOST_ID
                      from HOST H1, Host H2
                      where H1.HOST_NAME = H2.HOST_NAME and H1.HOST_ID != H2.HOST_ID
                      );
                                                        



-------------------------------------------------------------------------------------------------------

  
select CA.CALENDAR_DATE
from CALENDAR CA, LISTING L
where CA.LISTING_ID = L.LISTING_ID and CA.AVAILABLE = 't' and L.LISTING_ID IN (select L1.LISTING_ID
                                                                              from LISTING L1, HOST H
                                                                              where L1.HOST_ID = H.HOST_ID and H.HOST_NAME = 'Viajes Eco'
                                                                              );
                                                                              
                                                                              
-------------------------------------------------------------------------------------------------------



select H.HOST_ID, H.HOST_NAME
from HOST H 
where H.HOST_ID IN (select L.HOST_ID
                    from LISTING L
                    group by L.HOST_ID having COUNT(*) = 1
                   );
                                                        
-------------------------------------------------------------------------------------------------------
      
      
create view wifi as 
  select LA.LISTING_ID
  from AMENITIES AM, LISTING_AMENITIES LA                                     
  where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'Wifi';
  
select AVG(CD1.PRICE) - AVG(CD2.PRICE)
from COSTS_DETAILS CD1, COSTS_DETAILS CD2
where CD1.LISTING_ID in (select * from wifi)                                                       
and CD2.LISTING_ID not in (select * from wifi);

-------------------------------------------------------------------------------------------------------
                                                        
                                                 
              
select AVG(CD1.PRICE) - AVG(CD2.PRICE)
from COSTS_DETAILS CD1, COSTS_DETAILS CD2
where CD1.LISTING_ID IN (select MD.LISTING_ID
                         from MATERIAL_DESCRIPTION MD                                    
                         where MD.BEDS = 8)

and CD1.LISTING_ID IN (select L.LISTING_ID
                      from LISTING L
                      where L.CITY = 'Berlin')
                                                        
and CD2.LISTING_ID not in (select MD.LISTING_ID
                         from MATERIAL_DESCRIPTION MD                                    
                         where MD.BEDS = 8)  

and CD2.LISTING_ID IN (select L.LISTING_ID
                      from LISTING L
                      where L.CITY = 'Madrid');     
                      
                      
                      
-------------------------------------------------------------------------------------------------------


select * from 
  (select H.HOST_ID , H.HOST_NAME
    from LISTING L, HOST H                                          --not sure if working. need to test
    where L.COUNTRY = 'Spain' and L.HOST_ID = H.HOST_ID
    group by L.HOST_ID, H.HOST_NAME, H.HOST_ID
    order by COUNT(*) DESC)
    
    where rownum <= 10;



-------------------------------------------------------------------------------------------------------

select * from 
(select L.LISTING_ID, L.LISTING_NAME
from LISTING L, REVIEWS_SCORES RS
where L.LISTING_ID = RS.LISTING_ID 
  and L.LISTING_ID IN (select L1.LISTING_ID
                      from LISTING L1, MATERIAL_DESCRIPTION MD
                      where L.CITY = 'Barcelona' and MD.LISTING_ID = L.LISTING_ID and MD.PROPERTY_TYPE = 'Apartment')
order by RS.REVIEW_SCORES_RATING DESC)
where rownum <=10;



--queries for milestone 3 (in order)

QUERY NO1

select count(*)
from HOST h, LISTING l1
where h.HOST_ID in 
(select distinct(l2.HOST_ID) from LISTING l2, MATERIAL_DESCRIPTION md where l2.LISTING_ID = md.LISTING_ID and md.SQUARE_FEET is not null)
 and h.HOST_ID = l1.HOST_ID  group by l1.CITY order by l1.CITY asc;

------------------------------------------------------------------------------------------------------
QUERY NO2

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
------------------------------------------------------------------------------------------------------
QUERY NO3

select h.HOST_ID, h.HOST_NAME
from
(select HOST_ID, rank() over(order by nbr desc) as rnk
from
(select L.HOST_ID, count(*) as nbr
from LISTING l
group by l.HOST_ID)) ranked,
HOST h 

where h.HOST_ID = ranked.HOST_ID and ranked.rnk = 1;
------------------------------------------------------------------------------------------------------
QUERY NO4

select * from

(select AVG(cal.PRICE) as average, cal.LISTING_ID from

(select l.LISTING_ID from
LISTING l, MATERIAL_DESCRIPTION md, REVIEWS_SCORES rs, LISTING_DETAILS ld
where l.LISTING_ID = md.LISTING_ID and l.LISTING_ID = rs.LISTING_ID and
l.LISTING_ID = ld.LISTING_ID and l.CITY = 'Berlin' and md.PROPERTY_TYPE = 'Apartment'and
md.BEDS >= 2 and rs.REVIEW_SCORES_LOCATION >= 8 and ld.CANCELLATION_POLICY = 'flexible' and
l.HOST_ID IN (
  select hv.HOST_ID
  from HOST_VERIFICATIONS hv, VERIFICATIONS v
  where hv.VERIFICATION_ID = v.VERIFICATION_ID and v.VERIFICATION_NAME LIKE '%government_id%')) filtered,

CALENDAR cal
where cal.LISTING_ID = filtered.LISTING_ID and cal.CALENDAR_DATE between date'2019-03-01' and date'2019-04-30'
and cal.AVAILABLE = 't' group by cal.LISTING_ID order by average asc) averaged

where rownum <= 5;
------------------------------------------------------------------------------------------------------
QUERY NO5

select * from

(select filtered.LISTING_ID, md.ACCOMODATES, ROW_NUMBER() over(partition by md.ACCOMODATES order by rs.REVIEW_SCORES_RATING desc) as ranked
from
(select facilities.LISTING_ID from
(select la.LISTING_ID, count(*) as counted
from AMENITIES am, LISTING_AMENITIES la
where la.AMENITY_ID = am.AMENITY_ID and
(am.AMENITY_NAME = 'Wifi' or am.AMENITY_NAME = 'Internet' or
am.AMENITY_NAME = 'TV' or am.AMENITY_NAME = 'Free street parking')
group by la.LISTING_ID) facilities

where facilities.counted >= 2) filtered,

MATERIAL_DESCRIPTION md, REVIEWS_SCORES rs

where filtered.LISTING_ID = md.LISTING_ID and rs.LISTING_ID = filtered.LISTING_ID) rnk

where ranked <= 5
;
------------------------------------------------------------------------------------------------------
QUERY NO6
select HOST_ID, LISTING_ID 
from(
select HOST_ID, LISTING_ID, ROW_NUMBER() over(partition by HOST_ID order by counted desc) as r
from
(select distinct(l.LISTING_ID), l.HOST_ID, count(*) over(partition by l.LISTING_ID) as counted
from LISTING l, REVIEWS r
where l.LISTING_ID = r.LISTING_ID))

where r <= 3;
------------------------------------------------------------------------------------------------------
QUERY NO7

select AMENITY_NAME, NEIGHBORHOOD 

from 

(select AMENITY_NAME, AMEN_COUNT, NEIGHBORHOOD , row_number() over(partition by ordered_data.NEIGHBORHOOD order by AMEN_COUNT desc) as rank --rank the data with respect to neighborhood

from 

  (select distinct AMENITY_NAME, AMEN_COUNT, NEIGHBORHOOD 
  from 

    (select AM.AMENITY_NAME, count(AMENITY_NAME) over(partition by LOC.NEIGHBORHOOD, AM.AMENITY_NAME) as amen_count, LOC.NEIGHBORHOOD   --count the amenities with respect to their names
    from LISTING_LOCATION LOC, AMENITIES AM, LISTING_AMENITIES LA
    where LOC.LISTING_ID = LA.LISTING_ID and LA.AMENITY_ID = AM.AMENITY_ID and LOC.LISTING_ID in (

      select L.LISTING_ID
      from LISTING L, MATERIAL_DESCRIPTION MD
      where L.LISTING_ID = MD.LISTING_ID and L.CITY = 'Berlin' and MD.ROOM_TYPE = 'Private room'    --listings with private room in berlin

      )
    ) data_amen

  order by data_amen.NEIGHBORHOOD, data_amen.amen_count desc) ordered_data    --order the data with respect to the neighborhood
  ) ranked_data

where ranked_data.rank <= 3   --select data with rank smaller than 3 ==>  select first 3 for each neighborhood

;


------------------------------------------------------------------------------------------------------
QUERY NO8

create view number_of_host_verif as
  select count(*) as verifications, HV.HOST_ID
  from HOST_VERIFICATIONS HV
  group by HV.HOST_ID;

select avg(average_most.avg_m - average_least.avg_l) as diff
from 
 
 (select coalesce(avg(RS1.REVIEW_SCORES_COMMUNICATION),0) as avg_m        --coalesce force result to be 0 if avg resturn NULL (e.g avg on empty set)
 from
 
    (select h.HOST_ID
    from      
      (select n.HOST_ID
      from number_of_host_verif n
      order by n.verifications desc) h     --host with the most verification
    where rownum = 1) host_most ,
    
    LISTING L1, REVIEWS_SCORES RS1
    
    where L1.LISTING_ID = RS1.LISTING_ID and L1.HOST_ID = host_most.HOST_ID and RS1.REVIEW_SCORES_COMMUNICATION is not null
  ) average_most

  ,

   (select coalesce(avg(RS2.REVIEW_SCORES_COMMUNICATION),0) as avg_l
    from
 
    (select h2.HOST_ID
    from      
      (select n2.HOST_ID
      from number_of_host_verif n2
      order by n2.verifications asc) h2     --host with the least verification
    where rownum = 1) host_least ,
    
    LISTING L2, REVIEWS_SCORES RS2
    
    where L2.LISTING_ID = RS2.LISTING_ID and L2.HOST_ID = host_least.HOST_ID and RS2.REVIEW_SCORES_COMMUNICATION is not null
  ) average_least;

------------------------------------------------------------------------------------------------------
QUERY NO9

select ROOM_TYPE, CITY from
(select ROOM_TYPE, CITY, rank() over(partition by ROOM_TYPE order by total desc) as rank
from

(select distinct(ROOM_TYPE), sum(rev_per_list) over(partition by CITY, ROOM_TYPE) as total, CITY from

LISTING l,

(select distinct(rev.LISTING_ID), count(*) over(partition by rev.LISTING_ID) as rev_per_list
from REVIEWS rev) rpl,

(select md.LISTING_ID, md.ROOM_TYPE, AVG(md.ACCOMODATES) over(partition by md.ROOM_TYPE) as average_per_room_type
from MATERIAL_DESCRIPTION md) av

where rpl.LISTING_ID = av.LISTING_ID and l.LISTING_ID = av.LISTING_ID and average_per_room_type > 3))

where rank = 1
;

------------------------------------------------------------------------------------------------------
QUERY NO10

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
------------------------------------------------------------------------------------------------------
QUERY NO11

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
------------------------------------------------------------------------------------------------------
QUERY NO12

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
