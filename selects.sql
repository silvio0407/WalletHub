
select * from log_informations;


#drop table log_informations;

SELECT * FROM LOG_INFORMATIONS WHERE date_format(DATA_REQUEST, "%y-%m-%d");

SELECT * FROM LOG_INFORMATIONS 
WHERE DATA_REQUEST >=date_format('2017-01-01 00:00:12', "%y-%m-%d %H:%i:%s") AND DATA_REQUEST <=date_format('2017-01-02 00:20:00', "%y-%m-%d %H:%i:%s");

select DATE_FORMAT(DATA_REQUEST,'%y-%m-%d') from LOG_INFORMATIONS;

SELECT * FROM LOG_INFORMATIONS 
WHERE DATA_REQUEST between date_format('2017-01-01 00:00:12', "%y-%m-%d %H:%i:%s") AND date_format('2017-01-02 00:20:00', "%y-%m-%d %H:%i:%s");


SELECT *
FROM LOG_INFORMATIONS
WHERE DATA_REQUEST BETWEEN '2017-01-01 00:00:00' AND '2017-01-02 00:20:59';

SELECT * FROM LOG_INFORMATIONS 
WHERE DATA_REQUEST = date_format('2017-01-01 00:00:12', "%y-%m-%d %H:%i:%s");

SELECT * FROM LOG_INFORMATIONS 
WHERE DATA_REQUEST >= date_format('2017-01-01 00:00:12', "%y-%m-%d %H:%i:%s") AND DATA_REQUEST <= date_format('2017-01-01 00:00:21', "%y-%m-%d %H:%i:%s") ;

select COUNT(ip) AS TOTAL_REQUEST , ip AS IP_REQUEST
from LOG_INFORMATIONS 
where DATA_REQUEST >= date_format('2017-01-01 00:00:12', "%y-%m-%d %H:%i:%s")
group by ip
having count(ip) > 100;

SELECT * FROM LOG_INFORMATIONS WHERE IP = '192.168.169.194';
