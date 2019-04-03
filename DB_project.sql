
--queries for milestone 2 (in order)

select average(CD.PRICE)
from COSTS_DETAILS CD , LISTING L
where L.LISTING_ID = CD.LISTING_ID AND L.LISTING_ID IN (Select M.LISTING_ID

                                                        from MATERIAL_DESCRIPTION M
                                                        where M.BEDROOMS = 8);
                                                        
   
   
-------------------------------------------------------------------------------------------------------
                                   
select average(RS.REVIEW_SCORES_CLEANLINESS)
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
                    where L.LISTING_ID = CA.LISTING_ID and CA.AVAILABLE = 't' and CA.DATE >= '2019-03-01' and CA.DATE <= '2019-09-30'
                    );
 
 
 
------------------------------------------------------------------------------------------------------- 
                   
                    
select COUNT(*)
from LISTING L, HOST H
where L.HOST_ID = H.HOST_ID and H.HOST_ID IN (Select H1.HOST_ID
                      from HOST H1, Host H2
                      where H1.HOST_NAME = H2.HOST_NAME and H1.HOST_ID != H2.HOST_ID
                      );
                                                        



-------------------------------------------------------------------------------------------------------

  
select CA.DATE
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
      
      
with with_wifi as (select LA.LISTING_ID
                         from AMENITIES AM, LISTING_AMENITIES LA                                     
                         where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'WIFI')
                         
         
select average(CD1.PRICE) - average(CD2.PRICE)
from COSTS_DETAILS CD1, COSTS_DETAILS CD2
where CD1.LISTING_ID in with_wifi
                                                        
and CD2.LISTING_ID not in with_wifi


-------------------------------------------------------------------------------------------------------
                                                        
                                                 
              
with 8_beds as (select MD.LISTING_ID
                         from MATERIAL_DESCRIPTION MD                                    
                         where MD.BEDS = 8)   
         
select average(CD1.PRICE) - average(CD2.PRICE)
from COSTS_DETAILS CD1, COSTS_DETAILS CD2
where CD1.LISTING_ID IN 8_beds

and CD1.LISTING_ID IN (select L.LISTING_ID
                      from LISTING L
                      where L.CITY = 'Berlin');
                                                        
and CD2.LISTING_ID not in 8_beds  

and CD2.LISTING_ID IN (select L.LISTING_ID
                      from LISTING L
                      where L.CITY = 'Madrid');     
                      
                      
                      
                      
-------------------------------------------------------------------------------------------------------


select H.HOST_ID , H.HOST_NAME
                    from LISTING L, HOST H                                          --not sure if working. need to test
                    where L.COUNTRY = 'Spain' and L.HOST_ID = H.HOST_ID
                    group by L.HOST_ID
                    order by COUNT(*) DESC
                    
                    LIMIT 10




-------------------------------------------------------------------------------------------------------



              
select L.LISTING_ID, L.NAME
from LISTING L, REVIEWS_SCORES RS
where L.LISTING_ID IN (select L1.LISTING_ID
                      from LISTIING L1, MATERIAL_DESCRIPTION MD
                      where L.CITY = 'Barcelona' and MD.LISTING_ID = L.LISTING_ID and MD.PROPERTY_TYPE = 'appartment');
order by RS.REVIEW_SCORES_RATING DESC

LIMIT 10
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
                     
                                                        
                                                        
                                                        