USE charactersheet;

WITH amount AS(
	SELECT date,item, COUNT(item) as count FROM other_items
    GROUP BY date, item
)
SELECT DISTINCT a1.date, a1.item FROM amount a1
RIGHT JOIN amount a2 ON a1.count > a2.count WHERE a1.item != a2.item