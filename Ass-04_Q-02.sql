# Create a Stored procedure to retrieve average sales of each product in a month. Month and year will be input parameter to function.

DELIMITER //
CREATE PROCEDURE AverageSales (month INT, year INT)
BEGIN
  SELECT 
        Product.ID, 
        Product.Title,
        ROUND (AVG(ProductQuantity), 2) AS `Monthly Sales Average`
  FROM 
        orders
            JOIN
        order_detail ON order_detail.OrderID = orders.ID
            JOIN
        product ON product.ID = order_detail.ProductID
  WHERE MONTH(OrderTime) = month AND year = YEAR(OrderTime)
  GROUP BY product.ID;
END 
//
DELIMITER ;

CALL AverageSales(3, 2021);

DROP PROCEDURE AverageSales;

# Create a stored procedure to retrieve table having order detail with status for a given period. Start date and end date will be input parameter. Put validation on input dates like start date is less than end date. If start date is greater than end date take first date of month as start date.
DELIMITER //
CREATE PROCEDURE OrderInformation (startDate DATE, endDate DATE)
BEGIN
IF startDate > endDate THEN SET startDate = DATE_FORMAT(endDate, '%Y-%m-01');
END IF;
SELECT OrderID, ProductID, Status
  FROM order_detail 
  JOIN orders ON orders.ID = order_detail.OrderID
  WHERE OrderTime BETWEEN startDate AND endDate;
END //
DELIMITER ;

CALL OrderInformation('2021-03-18', '2021-03-05');

CALL OrderInformation('2021-03-04', '2021-03-18');

DROP PROCEDURE OrderInformation;