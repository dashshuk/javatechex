package org.uk.co.imperatives.exercise;


import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.uk.co.imperatives.exercise.dto.FilmRecommendationResult;
import org.uk.co.imperatives.exercise.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FilmRecommendationApp.class)
@ComponentScan(basePackages="org.uk.co.imperatives.exercise")
public class RecommendationTest {

    Logger logger = Logger.getLogger(RecommendationTest.class.getName());

    @Autowired
    private RecommendationService recommendationService;

    /**
     * EntityManager For all DB related operatons
     */
    @PersistenceContext
    private EntityManager testEntityManager;


    /**
     * Holds given expected results for customer 1
     */
    private static final List<FilmRecommendationResult> filmRecommendationResults = new ArrayList<>();
    private String newListOfInventoryIds = "";


    /**
     * Setup given expected result ready to assert
     */
    @BeforeAll
    public static void setupData(){

        Gson gson = new Gson();
        List<Map<?,?>> rs = gson.fromJson("""
                [
                  {
                    "id": 891,
                    "title": "Timberland Sky",
                    "description": "A Boring Display of a Man And a Dog who must Redeem a Girl in A U-Boat",
                    "category": "Classics",
                    "rating": "G",
                    "length": 69
                  },
                  {
                    "id": 358,
                    "title": "Gilmore Boiled",
                    "description": "A Unbelieveable Documentary of a Boat And a Husband who must Succumb a Student in A U-Boat",
                    "category": "Classics",
                    "rating": "R",
                    "length": 163
                  },
                  {
                    "id": 951,
                    "title": "Voyage Legally",
                    "description": "A Epic Tale of a Squirrel And a Hunter who must Conquer a Boy in An Abandoned Mine Shaft",
                    "category": "Classics",
                    "rating": "PG-13",
                    "length": 78
                  },
                  {
                    "id": 525,
                    "title": "Loathing Legally",
                    "description": "A Boring Epistle of a Pioneer And a Mad Scientist who must Escape a Frisbee in The Gulf of Mexico",
                    "category": "Classics",
                    "rating": "R",
                    "length": 140
                  },
                  {
                    "id": 445,
                    "title": "Hyde Doctor",
                    "description": "A Fanciful Documentary of a Boy And a Woman who must Redeem a Womanizer in A Jet Boat",
                    "category": "Classics",
                    "rating": "G",
                    "length": 100
                  },
                  {
                    "id": 471,
                    "title": "Island Exorcist",
                    "description": "A Fanciful Panorama of a Technical Writer And a Boy who must Find a Dentist in An Abandoned Fun House",
                    "category": "Classics",
                    "rating": "NC-17",
                    "length": 84
                  },
                  {
                    "id": 554,
                    "title": "Malkovich Pet",
                    "description": "A Intrepid Reflection of a Waitress And a A Shark who must Kill a Squirrel in The Outback",
                    "category": "Classics",
                    "rating": "G",
                    "length": 159
                  },
                  {
                    "id": 970,
                    "title": "Westward Seabiscuit",
                    "description": "A Lacklusture Tale of a Butler And a Husband who must Face a Boy in Ancient China",
                    "category": "Classics",
                    "rating": "NC-17",
                    "length": 52
                  },
                  {
                    "id": 266,
                    "title": "Dynamite Tarzan",
                    "description": "A Intrepid Documentary of a Forensic Psychologist And a Mad Scientist who must Face a Explorer in A U-Boat",
                    "category": "Classics",
                    "rating": "PG-13",
                    "length": 141
                  },
                  {
                    "id": 895,
                    "title": "Tomorrow Hustler",
                    "description": "A Thoughtful Story of a Moose And a Husband who must Face a Secret Agent in The Sahara Desert",
                    "category": "Classics",
                    "rating": "R",
                    "length": 142
                  }
                ]""", List.class);



        for (Map<?,?> item : rs){
            FilmRecommendationResult res = new FilmRecommendationResult();
            res.setId(Integer.valueOf(item.get("id").toString().substring(0,item.get("id").toString().indexOf("."))));
            res.setCategory((String) item.get("category"));
            res.setDescription((String) item.get("description"));
            res.setTitle((String) item.get("title"));
            res.setRating((String) item.get("rating"));
            res.setLength(Integer.valueOf(item.get("length").toString().substring(0,item.get("length").toString().indexOf("."))));
            filmRecommendationResults.add(res);
        }
    }

    @Test
    public void testRecommendationForCustomerId_1(){
        List<FilmRecommendationResult> rec = recommendationService.getRecommendations(1);
        Assertions.assertEquals(filmRecommendationResults, rec);
    }

    @Test
    @Transactional
    public void testRecommendationPostNewRentals(){
        newListOfInventoryIds = "";
        //add enough rentals (4 in this case) to film 951 to exceed the sample top film
        //used 951 as its the 2nd highest and easiest to add extra films
        createNewRentals();
        //Now Execute recommendation (check the query)
        List<FilmRecommendationResult> rec = recommendationService.getRecommendations(1);
        
        //Now the top recommendation should be film id 951 (if the query is correct)
        //So its not equal to the sample
        Assertions.assertNotEquals(filmRecommendationResults, rec);

        //Now the first film is 951
        Assertions.assertEquals(951, rec.getFirst().getId());
        deleteNewRentals(newListOfInventoryIds);

    }

    @Transactional
    public void createNewRentals(){
        try {

            String maxInventorySql = "select max(inventory_id) from inventory";
            String insertInventory = "insert into inventory (film_id, store_id, last_update)\n" +
                    " values (951,1,'2025-02-24 22:54:33')";

            for (int x = 0; x < 4; x++) {
                if (!newListOfInventoryIds.isEmpty()) {
                    newListOfInventoryIds = newListOfInventoryIds.concat(",");
                }
                //Add new inventory
                // Insert for new inventory for film 951
                testEntityManager.createNativeQuery(insertInventory).executeUpdate();
                //Extract latest Inventory id
                Integer inventoryId = (Integer) testEntityManager.createNativeQuery(maxInventorySql).getSingleResult();

                //Insert rental for new inventory
                String insertRentals = "insert into rental (rental_date, inventory_id, customer_id,return_date, staff_id,last_update)\n" +
                        "            values ('2025-02-24 22:54:33',\n" +
                        inventoryId + ",2,'2025-02-24 22:54:33',1,\n" +
                        "                    '2025-02-24 22:54:33')";
                testEntityManager.createNativeQuery(insertRentals).executeUpdate();
                newListOfInventoryIds = newListOfInventoryIds.concat(inventoryId.toString());
            }
        }catch(Exception exception){
            logger.log(Level.SEVERE, "Some DB error occured", exception);
        }
    }

    private void deleteNewRentals(String newListOfInventoryIds){
        String deleteNewRentals = "delete from rental where inventory_id in (" + newListOfInventoryIds + ")";
        testEntityManager.createNativeQuery(deleteNewRentals).executeUpdate();

        List<?> validateDeleteRentals = testEntityManager
                .createNativeQuery("select * from rental where inventory_id in (" + newListOfInventoryIds + ")")
                .getResultList();
        Assertions.assertEquals(0, validateDeleteRentals.size());

        String deleteNewInventories = "delete from inventory where inventory_id in (" + newListOfInventoryIds + ")";
        testEntityManager.createNativeQuery(deleteNewInventories).executeUpdate();

        List<?> validateDeleteInventories = testEntityManager
                .createNativeQuery("select * from inventory where inventory_id in (" + newListOfInventoryIds + ")")
                .getResultList();
        Assertions.assertEquals(0, validateDeleteInventories.size());
    }
}
