DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `ConsultaVuelosFecha`()
    NO SQL
SELECT * FROM vuelo WHERE fecha_vuelo > '2020-02-17'$$
DELIMITER ;