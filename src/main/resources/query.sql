select
    count(*) numberOfRecomendations,
    fc.film_id,
    title,
    description ,
    c."name" category,
    rating ,
    length
from
    rental r
        inner join customer c2 on
        c2.customer_id = r.customer_id
        inner join inventory i on
        r.inventory_id = i.inventory_id
        inner join film f on
        f.film_id = i.film_id
        inner join film_category fc on
        fc.film_id = f.film_id
        inner join category c on
        c.category_id = fc.category_id
where
    fc.category_id in (
        select
            category_id
        from
            (
                select
                    count(*) filmCount, c."name", c.category_id
                from
                    rental r
                        inner join customer c2 on
                        c2.customer_id = r.customer_id
                        inner join inventory i on
                        r.inventory_id = i.inventory_id
                        inner join film_category fcc on
                        fcc.film_id = i.film_id
                        inner join category c on
                        c.category_id = fcc.category_id
                where
                    c2.customer_id = 1
                group by
                    c."name", c.category_id
                order by
                    filmCount desc
                    limit 1 ) max_category )
  and not exists (
    select
        *
    from
        film f
            inner join inventory i2 on
            f.film_id = i2.film_id
                and f.film_id = fc.film_id
            inner join rental r2 on
            r2.inventory_id = i2.inventory_id
                and r2.customer_id = 1 )
group by
    category ,
    fc.film_id,
    fc.category_id,
    f.description ,
    f.title,
    f.rating ,
    f.length
order by
    numberOfRecomendations desc ,
    title asc
    limit 10;
