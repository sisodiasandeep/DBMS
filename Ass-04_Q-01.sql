# Create a function to calculate number of orders in a month. Month and year will be input parameter to function.
Delimiter 
//
CREATE function NumberOfOrders(month INT, year INT) returns INT DETERMINISTIC
BEGIN
DECLARE orderCount INT;
SELECT COUNT(ID) 
FROM orders 
WHERE month(OrderTime) = month 
AND year(OrderTime) = year
INTO orderCount;
RETURN orderCount;
END
// 
Delimiter;

SELECT NumberOfOrders(3, 2021) AS 'Number of Orders';

# Create a function to return month in a year having maximum orders. Year will be input parameter.
Delimiter 
//
CREATE function MaximumOrderMonth(year INT) returns VARCHAR(20) DETERMINISTIC
begin
DECLARE maxOrderMonth VARCHAR(20);
SELECT MONTHNAME(OrderTime)
FROM orders
WHERE year(OrderTime) = year
GROUP BY month(OrderTime)
HAVING COUNT(*) = 
    (SELECT MAX(totalOrdersOfMonth)
     FROM 
        (SELECT OrderTime, COUNT(*) totalOrdersOfMonth
         FROM orders
         WHERE year(OrderTime) = year
         GROUP BY month(OrderTime)) 
     AS maxOrderMonth)
INTO maxOrderMonth;
RETURN maxOrderMonth;
END
// 
Delimiter ;

SELECT MaximumOrderMonth(2021) AS 'Maximum Orders Month';