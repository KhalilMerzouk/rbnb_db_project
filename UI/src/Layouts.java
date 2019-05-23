import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public abstract class Layouts {


    /**
     * Setup layout and event handlers for the predefined queries layout
     * @return the layout for the predefined queries window
     */
    public static BorderPane getPredefinedQueriesLayout(){

        BorderPane b = new BorderPane();

        String query1 = "select AVG(CD.PRICE)\n" +
                "from COSTS_DETAILS CD , LISTING L\n" +
                "where L.LISTING_ID = CD.LISTING_ID AND L.LISTING_ID IN (Select M.LISTING_ID\n" +
                "\n" +
                "                                                        from MATERIAL_DESCRIPTION M\n" +
                "where M.BEDROOMS = 8)";

        String query2 = "select AVG(RS.REVIEW_SCORES_CLEANLINESS)\n" +
                "from REVIEWS_SCORES RS, LISTING L\n" +
                "where RS.LISTING_ID = L.LISTING_ID AND L.LISTING_ID IN (select LA.LISTING_ID\n" +
                "                                                        from AMENITIES AM, LISTING_AMENITIES LA\n" +
                "                                                        where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'TV'\n" +
                ")";

        String query3 = "select * \n" +
                "from HOST H\n" +
                "where H.HOST_ID IN (select L.HOST_ID\n" +
                "                    from LISTING L, CALENDAR CA\n" +
                "                    where L.LISTING_ID = CA.LISTING_ID and CA.AVAILABLE = 't' and CA.CALENDAR_DATE >= '01-MAR-19' and CA.CALENDAR_DATE <= '30-SEP-19'\n" +
                ")";

        String query4 = "select COUNT(*)\n" +
                "from LISTING L, HOST H\n" +
                "where L.HOST_ID = H.HOST_ID and H.HOST_ID IN (Select H1.HOST_ID\n" +
                "                      from HOST H1, Host H2\n" +
                "                      where H1.HOST_NAME = H2.HOST_NAME and H1.HOST_ID != H2.HOST_ID\n" +
                ")";

        String query5 = "select CA.CALENDAR_DATE\n" +
                "from CALENDAR CA, LISTING L\n" +
                "where CA.LISTING_ID = L.LISTING_ID and CA.AVAILABLE = 't' and L.LISTING_ID IN (select L1.LISTING_ID\n" +
                "                                                                              from LISTING L1, HOST H\n" +
                "                                                                              where L1.HOST_ID = H.HOST_ID and H.HOST_NAME = 'Viajes Eco'\n" +
                ")";

        String query6 = "select H.HOST_ID, H.HOST_NAME\n" +
                "from HOST H \n" +
                "where H.HOST_ID IN (select L.HOST_ID\n" +
                "                    from LISTING L\n" +
                "                    group by L.HOST_ID having COUNT(*) = 1\n" +
                ")";

        String query7 = "select AVG(CD1.PRICE) - AVG(CD2.PRICE)\n" +
                "from COSTS_DETAILS CD1, COSTS_DETAILS CD2\n" +
                "where CD1.LISTING_ID in (select LA.LISTING_ID\n" +
                "               from AMENITIES AM, LISTING_AMENITIES LA\n" +
                "               where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'Wifi')\n" +
                "                                                        \n" +
                "and CD2.LISTING_ID not in (select LA.LISTING_ID\n" +
                "               from AMENITIES AM, LISTING_AMENITIES LA     \n" +
                "               where AM.AMENITY_ID = LA.AMENITY_ID and AM.AMENITY_NAME = 'Wifi')\n";

        String query8 = "select AVG(CD1.PRICE) - AVG(CD2.PRICE)\n" +
                "from COSTS_DETAILS CD1, COSTS_DETAILS CD2\n" +
                "where CD1.LISTING_ID IN (select MD.LISTING_ID\n" +
                "                         from MATERIAL_DESCRIPTION MD                                    \n" +
                "                         where MD.BEDS = 8)\n" +
                "\n" +
                "and CD1.LISTING_ID IN (select L.LISTING_ID\n" +
                "                      from LISTING L\n" +
                "                      where L.CITY = 'Berlin')\n" +
                "                                                        \n" +
                "and CD2.LISTING_ID not in (select MD.LISTING_ID\n" +
                "                         from MATERIAL_DESCRIPTION MD                                    \n" +
                "                         where MD.BEDS = 8)  \n" +
                "\n" +
                "and CD2.LISTING_ID IN (select L.LISTING_ID\n" +
                "                      from LISTING L\n" +
                "where L.CITY = 'Madrid') ";

        String query9 = "select * from \n" +
                "  (select H.HOST_ID , H.HOST_NAME\n" +
                "    from LISTING L, HOST H\n" +
                "    where L.COUNTRY = 'Spain' and L.HOST_ID = H.HOST_ID\n" +
                "    group by L.HOST_ID, H.HOST_NAME, H.HOST_ID\n" +
                "    order by COUNT(*) DESC)\n" +
                "    \n" +
                "    where rownum <= 10\n" +
                "\n";

        String query10 = "select * from \n" +
                "(select L.LISTING_ID, L.LISTING_NAME\n" +
                "from LISTING L, REVIEWS_SCORES RS\n" +
                "where L.LISTING_ID = RS.LISTING_ID \n" +
                "  and L.LISTING_ID IN (select L1.LISTING_ID\n" +
                "                      from LISTING L1, MATERIAL_DESCRIPTION MD\n" +
                "                      where L.CITY = 'Barcelona' and MD.LISTING_ID = L.LISTING_ID and MD.PROPERTY_TYPE = 'Apartment')\n" +
                "order by RS.REVIEW_SCORES_RATING DESC)\n" +
                "where rownum <=10";



        String query11 = "select count(distinct(h.HOST_ID)), l1.CITY\n" +
                "from HOST h, LISTING l1,  MATERIAL_DESCRIPTION md\n" +
                "where h.HOST_ID = l1.HOST_ID and l1.LISTING_ID = md.LISTING_ID and md.SQUARE_FEET is not null\n" +
                "group by l1.CITY order by l1.CITY asc";

        String query12 = "select * from\n" +
                "\n" +
                "(select distinct(med_per_ng.NEIGHBORHOOD), REVIEW_SCORES_RATING from\n" +
                "\n" +
                "(select distinct(loc.NEIGHBORHOOD), floor((count(*) over(partition by loc.NEIGHBORHOOD)+1)/2) as median_elem_per_ng\n" +
                "from REVIEWS_SCORES rs, LISTING l, LISTING_LOCATION loc\n" +
                "where rs.LISTING_ID = l.LISTING_ID and loc.LISTING_ID = l.LISTING_ID and rs.REVIEW_SCORES_RATING is not null and l.CITY = 'Madrid') med_per_ng,\n" +
                "\n" +
                "(select loc.NEIGHBORHOOD, rs.REVIEW_SCORES_RATING, ROW_NUMBER() over(partition by loc.NEIGHBORHOOD order by rs.REVIEW_SCORES_RATING desc) as rnum\n" +
                "from REVIEWS_SCORES rs, LISTING_LOCATION loc, LISTING l\n" +
                "where rs.LISTING_ID = loc.LISTING_ID and l.LISTING_ID = rs.LISTING_ID and l.CITY = 'Madrid' and rs.REVIEW_SCORES_RATING is not null) ranked_by_ng_and_rev\n" +
                "\n" +
                "where med_per_ng.NEIGHBORHOOD = ranked_by_ng_and_rev.NEIGHBORHOOD and median_elem_per_ng = rnum\n" +
                "order by REVIEW_SCORES_RATING desc)\n" +
                "\n" +
                "where rownum <= 5\n";


        String query13 = "select h.HOST_ID, h.HOST_NAME\n" +
                "from\n" +
                "(select HOST_ID, rank() over(order by nbr desc) as rnk\n" +
                "from\n" +
                "(select L.HOST_ID, count(*) as nbr\n" +
                "from LISTING l\n" +
                "group by l.HOST_ID)) ranked,\n" +
                "HOST h \n" +
                "\n" +
                "where h.HOST_ID = ranked.HOST_ID and ranked.rnk = 1";


        String query14 = "select * from\n" +
                "\n" +
                "(select AVG(cal.PRICE) as average, cal.LISTING_ID from\n" +
                "\n" +
                "(select l.LISTING_ID from\n" +
                "LISTING l, MATERIAL_DESCRIPTION md, REVIEWS_SCORES rs, LISTING_DETAILS ld\n" +
                "where l.LISTING_ID = md.LISTING_ID and l.LISTING_ID = rs.LISTING_ID and\n" +
                "l.LISTING_ID = ld.LISTING_ID and l.CITY = 'Berlin' and md.PROPERTY_TYPE = 'Apartment'and\n" +
                "md.BEDS >= 2 and rs.REVIEW_SCORES_LOCATION >= 8 and ld.CANCELLATION_POLICY = 'flexible' and\n" +
                "l.HOST_ID IN (\n" +
                "  select hv.HOST_ID\n" +
                "  from HOST_VERIFICATIONS hv, VERIFICATIONS v\n" +
                "  where hv.VERIFICATION_ID = v.VERIFICATION_ID and v.VERIFICATION_NAME LIKE '%government_id%')) filtered,\n" +
                "\n" +
                "CALENDAR cal\n" +
                "where cal.LISTING_ID = filtered.LISTING_ID and cal.CALENDAR_DATE between date'2019-03-01' and date'2019-04-30'\n" +
                "and cal.AVAILABLE = 't' group by cal.LISTING_ID order by average asc) averaged\n" +
                "\n" +
                "where rownum <= 5";


        String query15 = "select * from\n" +
                "\n" +
                "(select filtered.LISTING_ID, md.ACCOMODATES, ROW_NUMBER() over(partition by md.ACCOMODATES order by rs.REVIEW_SCORES_RATING desc) as ranked\n" +
                "from\n" +
                "(select facilities.LISTING_ID from\n" +
                "(select la.LISTING_ID, count(*) as counted\n" +
                "from AMENITIES am, LISTING_AMENITIES la\n" +
                "where la.AMENITY_ID = am.AMENITY_ID and\n" +
                "(am.AMENITY_NAME = 'Wifi' or am.AMENITY_NAME = 'Internet' or\n" +
                "am.AMENITY_NAME = 'TV' or am.AMENITY_NAME = 'Free street parking')\n" +
                "group by la.LISTING_ID) facilities\n" +
                "\n" +
                "where facilities.counted >= 2) filtered,\n" +
                "\n" +
                "MATERIAL_DESCRIPTION md, REVIEWS_SCORES rs\n" +
                "\n" +
                "where filtered.LISTING_ID = md.LISTING_ID and rs.LISTING_ID = filtered.LISTING_ID) rnk\n" +
                "\n" +
                "where ranked <= 5\n";


        String query16 = "select HOST_ID, LISTING_ID \n" +
                "from(\n" +
                "select HOST_ID, LISTING_ID, ROW_NUMBER() over(partition by HOST_ID order by counted desc) as r\n" +
                "from\n" +
                "(select distinct(l.LISTING_ID), l.HOST_ID, count(*) over(partition by l.LISTING_ID) as counted\n" +
                "from LISTING l, REVIEWS r\n" +
                "where l.LISTING_ID = r.LISTING_ID))\n" +
                "\n" +
                "where r <= 3";


        String query17 = "select AMENITY_NAME, NEIGHBORHOOD \n" +
                "\n" +
                "from \n" +
                "\n" +
                "(select AMENITY_NAME, AMEN_COUNT, NEIGHBORHOOD , row_number() over(partition by ordered_data.NEIGHBORHOOD order by AMEN_COUNT desc) as rank --rank the data with respect to neighborhood\n" +
                "\n" +
                "from \n" +
                "\n" +
                "  (select distinct AMENITY_NAME, AMEN_COUNT, NEIGHBORHOOD \n" +
                "  from \n" +
                "\n" +
                "    (select AM.AMENITY_NAME, count(AMENITY_NAME) over(partition by LOC.NEIGHBORHOOD, AM.AMENITY_NAME) as amen_count, LOC.NEIGHBORHOOD   --count the amenities with respect to their names\n" +
                "    from LISTING_LOCATION LOC, AMENITIES AM, LISTING_AMENITIES LA\n" +
                "    where LOC.LISTING_ID = LA.LISTING_ID and LA.AMENITY_ID = AM.AMENITY_ID and LOC.LISTING_ID in (\n" +
                "\n" +
                "      select L.LISTING_ID\n" +
                "      from LISTING L, MATERIAL_DESCRIPTION MD\n" +
                "      where L.LISTING_ID = MD.LISTING_ID and L.CITY = 'Berlin' and MD.ROOM_TYPE = 'Private room'    --listings with private room in berlin\n" +
                "\n" +
                "      )\n" +
                "    ) data_amen\n" +
                "\n" +
                "  order by data_amen.NEIGHBORHOOD, data_amen.amen_count desc) ordered_data    --order the data with respect to the neighborhood\n" +
                "  ) ranked_data\n" +
                "\n" +
                "where ranked_data.rank <= 3   --select data with rank smaller than 3 ==>  select first 3 for each neighborhood\n";


        String query18 = "create view number_of_host_verif as\n" +
                "  select count(*) as verifications, HV.HOST_ID\n" +
                "  from HOST_VERIFICATIONS HV\n" +
                "  group by HV.HOST_ID;\n" +
                "\n" +
                "select avg(average_most.avg_m - average_least.avg_l) as diff\n" +
                "from \n" +
                " \n" +
                " (select coalesce(avg(RS1.REVIEW_SCORES_COMMUNICATION),0) as avg_m        --coalesce force result to be 0 if avg resturn NULL (e.g avg on empty set)\n" +
                " from\n" +
                " \n" +
                "    (select h.HOST_ID\n" +
                "    from      \n" +
                "      (select n.HOST_ID\n" +
                "      from number_of_host_verif n\n" +
                "      order by n.verifications desc) h     --host with the most verification\n" +
                "    where rownum = 1) host_most ,\n" +
                "    \n" +
                "    LISTING L1, REVIEWS_SCORES RS1\n" +
                "    \n" +
                "    where L1.LISTING_ID = RS1.LISTING_ID and L1.HOST_ID = host_most.HOST_ID and RS1.REVIEW_SCORES_COMMUNICATION is not null\n" +
                "  ) average_most\n" +
                "\n" +
                "  ,\n" +
                "\n" +
                "   (select coalesce(avg(RS2.REVIEW_SCORES_COMMUNICATION),0) as avg_l\n" +
                "    from\n" +
                " \n" +
                "    (select h2.HOST_ID\n" +
                "    from      \n" +
                "      (select n2.HOST_ID\n" +
                "      from number_of_host_verif n2\n" +
                "      order by n2.verifications asc) h2     --host with the least verification\n" +
                "    where rownum = 1) host_least ,\n" +
                "    \n" +
                "    LISTING L2, REVIEWS_SCORES RS2\n" +
                "    \n" +
                "    where L2.LISTING_ID = RS2.LISTING_ID and L2.HOST_ID = host_least.HOST_ID and RS2.REVIEW_SCORES_COMMUNICATION is not null\n" +
                "  ) average_least\n";


        String query19 = "select * from (\n" +
                "\n" +
                "select city from \n" +
                "(select  sum(rev_per_list) over(partition by CITY) as total, CITY from    --rank cities by the number of reviews for the listing types with avg(accomodate) > 3\n" +
                "\n" +
                "LISTING l,\n" +
                "\n" +
                "(select distinct(rev.LISTING_ID), count(*) over(partition by rev.LISTING_ID) as rev_per_list    --count reviews per listing\n" +
                "from REVIEWS rev) rpl,\n" +
                "\n" +
                "(select md.LISTING_ID,  AVG(md.ACCOMODATES) over(partition by md.ROOM_TYPE) as average_per_room_type --compute average number of accomodate per room typee\n" +
                "from MATERIAL_DESCRIPTION md) av\n" +
                "\n" +
                "where rpl.LISTING_ID = av.LISTING_ID and l.LISTING_ID = av.LISTING_ID and av.average_per_room_type > 3)\n" +
                "\n" +
                "order by total desc\n" +
                "\n" +
                ")\n" +
                "\n" +
                "where rownum = 1\n" +
                ";\n";


        String query20 = "select total_listing.NEIGHBORHOOD\n" +
                "from \n" +
                "\n" +
                "(select count(*) as total, LOC2.NEIGHBORHOOD      --total number of listings per neighborhood\n" +
                "from LISTING_LOCATION LOC2, LISTING L3\n" +
                "where LOC2.LISTING_ID = L3.LISTING_ID and L3.CITY = 'Madrid' group by LOC2.NEIGHBORHOOD) total_listing \n" +
                "\n" +
                ",\n" +
                "\n" +
                "(select count(*) as occupied_listings, LOC.NEIGHBORHOOD\n" +
                "from LISTING_LOCATION LOC, LISTING L2\n" +
                "where L2.LISTING_ID = LOC.LISTING_ID and L2.CITY = 'Madrid' and L2.LISTING_ID in (\n" +
                "\n" +
                "  select distinct L1.LISTING_ID      --listing in madrid that were taken in 2019 \n" +
                "  from LISTING L1, CALENDAR CAL\n" +
                "  where CAL.CALENDAR_DATE >= date '2019-01-01' and CAL.AVAILABLE = 'f' and L1.LISTING_ID = CAL.LISTING_ID \n" +
                "  and L1.LISTING_ID in (\n" +
                "\n" +
                "    select L.LISTING_ID   --listing in madrid whose host is member since '2017-06-01' \n" +
                "    from LISTING L, HOST H\n" +
                "    where L.CITY = 'Madrid' and L.HOST_ID = H.HOST_ID and H.HOST_SINCE <= date '2017-06-01' \n" +
                "  )\n" +
                "\n" +
                ") group by LOC.NEIGHBORHOOD) filtered_listing\n" +
                "\n" +
                "where total_listing.NEIGHBORHOOD = filtered_listing.NEIGHBORHOOD and (filtered_listing.occupied_listings / total_listing.total) >= 0.5";



        String query21 = "select filtered.COUNTRY\n" +
                "from \n" +
                "\n" +
                "(select count(*) as available, L.COUNTRY\n" +
                "from LISTING L\n" +
                "where L.LISTING_ID in (\n" +
                " select distinct L1.LISTING_ID     \n" +
                "  from LISTING L1, CALENDAR CAL\n" +
                "  where CAL.CALENDAR_DATE >= date '2018-01-01' and CAL.CALENDAR_DATE < date '2019-01-01' and CAL.AVAILABLE = 't' and L1.LISTING_ID = CAL.LISTING_ID\n" +
                "  \n" +
                ") group by L.COUNTRY) filtered\n" +
                " \n" +
                ",\n" +
                "\n" +
                "(select count(*) total_listing, L2.COUNTRY\n" +
                "from LISTING L2\n" +
                "group by L2.COUNTRY) total\n" +
                "\n" +
                "where filtered.COUNTRY = total.COUNTRY and (filtered.available/ total.total_listing) >= 0.2";


        String query22 = "select total.NEIGHBORHOOD\n" +
                "from \n" +
                "\n" +
                "(select count(*) total_list, LOC.NEIGHBORHOOD\n" +
                "from LISTING_LOCATION LOC, LISTING L\n" +
                "where LOC.LISTING_ID = L.LISTING_ID and L.CITY = 'Barcelona' group by LOC.NEIGHBORHOOD) total\n" +
                "\n" +
                ",\n" +
                "\n" +
                "(\n" +
                "select count(*) strict_count, LOC.NEIGHBORHOOD\n" +
                "from LISTING_LOCATION LOC, LISTING_DETAILS LD, LISTING L2\n" +
                "where LOC.LISTING_ID = LD.LISTING_ID and L2.LISTING_ID = LD.LISTING_ID and L2.CITY = 'Barcelona' and LD.CANCELLATION_POLICY = 'strict_14_with_grace_period' group by LOC.NEIGHBORHOOD\n" +
                "\n" +
                ") filtered\n" +
                "\n" +
                "where total.NEIGHBORHOOD = filtered.NEIGHBORHOOD and (filtered.strict_count / total.total_list) >= 0.05";


        Button q1 = new Button("Q1");
        Button q2 = new Button("Q2");
        Button q3 = new Button("Q3");
        Button q4 = new Button("Q4");
        Button q5 = new Button("Q5");
        Button q6 = new Button("Q6");
        Button q7 = new Button("Q7");
        Button q8 = new Button("Q8");
        Button q9 = new Button("Q9");
        Button q10 = new Button("Q10");
        Button q11 = new Button("Q11");
        Button q12 = new Button("Q12");
        Button q13 = new Button("Q13");
        Button q14 = new Button("Q14");
        Button q15 = new Button("Q15");
        Button q16 = new Button("Q16");
        Button q17 = new Button("Q17");
        Button q18 = new Button("Q18");
        Button q19 = new Button("Q19");
        Button q20 = new Button("Q20");
        Button q21 = new Button("Q21");
        Button q22 = new Button("Q22");


        TextField paramQ1 = new TextField("enter number of bedrooms for Q1");
        paramQ1.setLayoutY(100);
        paramQ1.setLayoutX(70);

        TextField paramQ2 = new TextField("enter amenity for Q2");
        paramQ1.setLayoutY(120);
        paramQ1.setLayoutX(70);


        TextField paramQ5 = new TextField("enter host name for Q5");
        paramQ1.setLayoutY(180);
        paramQ1.setLayoutX(70);


        q1.setLayoutX(40);
        q1.setLayoutY(100);
        ArrayList<String> columnNames1 = new ArrayList<>();
        columnNames1.add("average");
        q1.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query1, b, columnNames1, paramQ1,"Q1"));

        q2.setLayoutX(40);
        q2.setLayoutY(120);
        ArrayList<String> columnNames2 = new ArrayList<>();
        columnNames2.add("average");
        q2.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query2, b, columnNames2, paramQ2, "Q2"));

        q3.setLayoutX(40);
        q3.setLayoutY(140);
        ArrayList<String> columnNames3 = new ArrayList<>();
        columnNames3.add("host_id");
        columnNames3.add("host_url");
        columnNames3.add("host_name");
        columnNames3.add("host_since");
        columnNames3.add("host_thumbnail_url");
        q3.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query3, b, columnNames3,null, null));

        q4.setLayoutX(40);
        q4.setLayoutY(160);
        ArrayList<String> columnNames4 = new ArrayList<>();
        columnNames4.add("count");
        q4.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query4, b, columnNames4,null, null));

        q5.setLayoutX(40);
        q5.setLayoutY(180);
        ArrayList<String> columnNames5 = new ArrayList<>();
        columnNames5.add("calendar_date");
        q5.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query5, b, columnNames5,null, null));

        q6.setLayoutX(40);
        q6.setLayoutY(200);
        ArrayList<String> columnNames6 = new ArrayList<>();
        columnNames6.add("host_id");
        columnNames6.add("host_name");
        q6.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query6, b, columnNames6,null, null));

        q7.setLayoutX(40);
        q7.setLayoutY(220);
        ArrayList<String> columnNames7 = new ArrayList<>();
        columnNames7.add("average_difference");
        q7.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query7, b, columnNames7,null, null));

        q8.setLayoutX(40);
        q8.setLayoutY(240);
        ArrayList<String> columnNames8 = new ArrayList<>();
        columnNames8.add("average_difference");
        q8.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query8, b, columnNames8,null, null));

        q9.setLayoutX(40);
        q9.setLayoutY(260);
        ArrayList<String> columnNames9 = new ArrayList<>();
        columnNames9.add("host_id");
        columnNames9.add("host_name");
        q9.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query9, b, columnNames9,null, null));

        q10.setLayoutX(40);
        q10.setLayoutY(280);
        ArrayList<String> columnNames10 = new ArrayList<>();
        columnNames10.add("listing_id");
        columnNames10.add("listing_name");
        q10.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query10, b, columnNames10,null, null));



        //For milestone 3 queries

        q11.setLayoutX(400);
        q11.setLayoutY(100);
        ArrayList<String> columnNames11 = new ArrayList<>();
        columnNames11.add("count");
        q11.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query11, b, columnNames11, null,null));

        q12.setLayoutX(400);
        q12.setLayoutY(120);
        ArrayList<String> columnNames12 = new ArrayList<>();
        columnNames12.add("neighborhood");
        columnNames12.add("review score rating");
        q12.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query12, b, columnNames12, null, null));

        q13.setLayoutX(400);
        q13.setLayoutY(140);
        ArrayList<String> columnNames13 = new ArrayList<>();
        columnNames13.add("host_id");
        columnNames13.add("host_name");
        q13.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query13, b, columnNames13,null, null));

        q14.setLayoutX(400);
        q14.setLayoutY(160);
        ArrayList<String> columnNames14 = new ArrayList<>();
        columnNames14.add("average");
        columnNames14.add("listing id");
        q14.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query14, b, columnNames14,null, null));

        q15.setLayoutX(400);
        q15.setLayoutY(180);
        ArrayList<String> columnNames15 = new ArrayList<>();
        columnNames15.add("listing id");
        columnNames15.add("number of people");
        columnNames15.add("rank");
        q15.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query15, b, columnNames15,null, null));

        q16.setLayoutX(400);
        q16.setLayoutY(200);
        ArrayList<String> columnNames16 = new ArrayList<>();
        columnNames16.add("host_id");
        columnNames16.add("listing id");
        q16.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query16, b, columnNames16,null, null));

        q17.setLayoutX(400);
        q17.setLayoutY(220);
        ArrayList<String> columnNames17 = new ArrayList<>();
        columnNames17.add("amenity name");
        columnNames17.add("neighborhood");
        q17.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query17, b, columnNames17,null, null));

        q18.setLayoutX(400);
        q18.setLayoutY(240);
        ArrayList<String> columnNames18 = new ArrayList<>();
        columnNames18.add("average_difference");
        q18.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query18, b, columnNames18,null, null));

        q19.setLayoutX(400);
        q19.setLayoutY(260);
        ArrayList<String> columnNames19 = new ArrayList<>();
        columnNames19.add("room type");
        columnNames19.add("city");
        q19.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query19, b, columnNames19,null, null));

        q20.setLayoutX(400);
        q20.setLayoutY(280);
        ArrayList<String> columnNames20 = new ArrayList<>();
        columnNames20.add("neighborhood");
        q20.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query20, b, columnNames20,null, null));

        q21.setLayoutX(400);
        q21.setLayoutY(300);
        ArrayList<String> columnNames21 = new ArrayList<>();
        columnNames21.add("country");
        q21.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query21, b, columnNames21,null, null));

        q22.setLayoutX(400);
        q22.setLayoutY(320);
        ArrayList<String> columnNames22 = new ArrayList<>();
        columnNames22.add("neighborhood");
        q22.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.executePredefined(query22, b, columnNames22,null, null));

        //add the buttons to the layout

        VBox vBoxLeft = new VBox();
        vBoxLeft.setSpacing(10);

        VBox vBoxRight = new VBox();
        vBoxRight.setSpacing(10);

        VBox paramBox = new VBox();
        vBoxLeft.setSpacing(10);

        vBoxLeft.getChildren().addAll(q1,q2,q3,q4,q5,q6,q7,q8,q9,q10);
        vBoxRight.getChildren().addAll(q11,q12,q13,q14,q15,q16,q17,q18,q19,q20,q21,q22);

        paramBox.getChildren().addAll(paramQ1, paramQ2, paramQ5);

        b.setBottom(paramBox);

        b.setLeft(vBoxLeft);
        b.setRight(vBoxRight);

        return b;

    }



    /**
     * Setup layout and event handlers for the insert/delete layout
     * @return the layout for the insert/delete window
     */
    public static BorderPane getInsertDeleteLayout(){

        BorderPane b = new BorderPane();

        //create buttons
        Button insert = new Button("Insert");
        Button delete = new Button("Delete");

        //insert buttons in a horizontal box
        HBox box = new HBox();
        box.getChildren().addAll(insert, delete);
        box.setSpacing(20);

        //insert into layout the checkboxes to choose table in which to insert/delete
        getTableCheckbox(b);


        insert.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {Controllers.insertIntoTables(b);});

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {Controllers.deleteFromTable(b);});

        b.setBottom(box);

        return b;
    }


    /**
     * Setup layout and event handlers for the search layout
     * @return the layout for the search window
     */
    public static BorderPane getSearchLayout(){

        BorderPane b = new BorderPane();

        TextField txt = new TextField("Please type your search here");

        Button searchButton = new Button("Search");

        b.setBottom(searchButton);
        b.setLeft(txt);

        //put checkboxes in the layout
        getTableCheckbox(b);

        //call controler's procedure if search button is clicked
        searchButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.search(txt.getText(), b) );


        return b;
    }


    /**
     * Method to put checkboxes to choose which table to perform a query on in the layout
     * @param b a borderpane in which the checkboxes will be inserted
     */
    public static void getTableCheckbox(BorderPane b){


        //create checkboxes, labels and add them to the layout

        HBox h1 = new HBox();
        HBox h2 = new HBox();
        HBox h3 = new HBox();



        CheckBox t1 = new CheckBox(Utils.tableToString(Table.HOST));
        CheckBox t2 = new CheckBox(Utils.tableToString(Table.REVIEWS));
        CheckBox t3 = new CheckBox(Utils.tableToString(Table.LISTING));



        t1.setMinWidth(300);
        t2.setMinWidth(300);
        t3.setMinWidth(300);


        h1.getChildren().addAll(t1,t2,t3);
        h2.getChildren().addAll(t2);
        h3.getChildren().addAll(t3);


        h1.setLayoutX(20);
        h1.setLayoutY(200);
        h2.setLayoutX(20);
        h2.setLayoutY(220);
        h3.setLayoutX(20);
        h3.setLayoutY(240);


        b.getChildren().addAll(h1,h2,h3);

    }


    /**
     * Build layout for insertion given a table
      * @param t table in which to perform the insertion
     * @return a layout for the insertion window
     */
    public static BorderPane getInsertLayout(Table t){


        BorderPane b = new BorderPane();

        Button insert = new Button("Insert "+Utils.tableToString(t));

        setupQuerySubmissionLayout(b, insert, Utils.getColumnsFromTable(t));

        insert.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.insertData(b, t));

        return b;
    }


    /**
     * Build layout for data deletion
     * @return the layout element for data deletion
     */
    public static Parent getDeleteLayout(Table t){

        BorderPane b = new BorderPane();

        Button delete = new Button("Delete "+Utils.tableToString(t));

        setupQuerySubmissionLayout(b, delete, Utils.getColumnsFromTable(t));

        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> Controllers.deleteData(b, t));


        return b;
    }


    /**
     * Build a layout used for form submission (insert and delete)
     * @param b the layout
     * @param submitButton the button used for submission
     * @param textFields the list of textfields that will appear in the layout
     */
    public static void setupQuerySubmissionLayout(BorderPane b, Button submitButton, ArrayList<String> textFields){


        HBox hBox = new HBox();
        hBox.getChildren().add(submitButton);

        b.setBottom(hBox);

        VBox box = new VBox();
        b.setCenter(box);

        //generate textFields
        ArrayList<TextField> fields = new ArrayList<>();
        textFields.forEach(t -> fields.add(new TextField(t)));

        //add them into the layout
        box.getChildren().addAll(fields);
        box.setSpacing(15);

    }

}
