package org.uk.co.imperatives.exercise.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.NamedNativeQuery;
import org.uk.co.imperatives.exercise.dto.FilmRecommendationResult;

/**
 * Class used purely to get native SQL to object conversion work conveniently via @SqlResultSetMapping
 * without having to explicitly code all the mappings
 */
@NamedNativeQuery(name = "FilmRecommendation",
        query= """
                select
                \tcount(*) numberOfRecomendations,
                \tfc.film_id,
                \ttitle,
                \tdescription ,
                \tc."name" category,
                \trating ,
                \tlength
                from
                \trental r
                inner join customer c2 on
                \tc2.customer_id = r.customer_id
                inner join inventory i on
                \tr.inventory_id = i.inventory_id
                inner join film f on
                \tf.film_id = i.film_id
                inner join film_category fc on
                \tfc.film_id = f.film_id
                inner join category c on
                \tc.category_id = fc.category_id
                where
                \tfc.category_id in (
                \tselect
                \t\tcategory_id
                \tfrom
                \t\t(
                \t\tselect
                \t\t\tcount(*) filmCount, c."name", c.category_id
                \t\tfrom
                \t\t\trental r
                \t\tinner join customer c2 on
                \t\t\tc2.customer_id = r.customer_id
                \t\tinner join inventory i on
                \t\t\tr.inventory_id = i.inventory_id
                \t\tinner join film_category fcc on
                \t\t\tfcc.film_id = i.film_id
                \t\tinner join category c on
                \t\t\tc.category_id = fcc.category_id
                \t\twhere
                \t\t\tc2.customer_id = ?
                \t\tgroup by
                \t\t\tc."name", c.category_id
                \t\torder by
                \t\t\tfilmCount desc
                \t\tlimit 1 ) max_category )\s
                \tand not exists (
                \tselect
                \t\t*
                \tfrom
                \t\tfilm f\s
                \t\tinner join inventory i2 on f.film_id =i2.film_id  and f.film_id =fc.film_id\s
                \t\tinner join rental r2 on r2.inventory_id =i2.inventory_id\s
                \t\tand r2.customer_id =?
                \t)
                
                group by
                \tcategory ,
                \tfc.film_id,
                \tfc.category_id,
                \tf.description ,
                \tf.title,
                \tf.rating ,
                \tf.length
                order by
                \tnumberOfRecomendations desc ,
                \ttitle asc
                limit 10;
                
                """,
        resultSetMapping = "FilmRecommendationResult")

@SqlResultSetMapping(name="FilmRecommendationResult",
        classes=@ConstructorResult(
                targetClass = FilmRecommendationResult.class,
                columns={
                        @ColumnResult(name="film_id", type=Integer.class),
                        @ColumnResult(name="title", type=String.class),
                        @ColumnResult(name="description", type=String.class),
                        @ColumnResult(name="category", type=String.class),
                        @ColumnResult(name="rating", type=String.class),
                        @ColumnResult(name="length", type=Integer.class)
                }
        )
)

@Entity
public class FilmRecommendation {
    @Id
    private Long id;
}

