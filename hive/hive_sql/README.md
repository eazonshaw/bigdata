# 作业
## 描述
### hive_sql_test1.t_user表
* t_user观众表共6000+条数据
* 字段为：UserID, Sex, Age, Occupation, Zipcode
* 字段中文解释：用户id，性别，年龄，职业，邮编

### hive_sql_test1.t_movie表
* t_movie电影表共3000+条数据
* 字段为：MovieID, MovieName, MovieType
* 字段中文解释：电影ID，电影名，电影类型

### hive_sql_test1.t_rating表
* t_rating影评表100万+条数据
* 字段为：UserID, MovieID, Rate, Times
* 字段中文解释：用户ID，电影ID，评分，评分时间

## 题目一
* 简单：展示电影ID为2116这部电影各年龄段的平均影评分

```sql
SELECT t2.age age, AVG(t1.rate) avgrate 
FROM hive_sql_test1.t_rating t1
    LEFT JOIN hive_sql_test1.t_user t2 ON t1.userid = t2.userid
WHERE t1.movieid = 2116
GROUP BY T2.age
```

![img1](https://github.com/eazonshaw/bigdata/blob/master/hive/hive_sql/img/img1.png)

## 题目二
* 中等：找出男性评分最高且评分次数超过50次的10部电影，展示电影名，平均影评分和评分次数

```sql
SELECT t1.moviename name, avg (t2.rate) avgrate, count (t2.times) total
FROM t_movie t1
         LEFT JOIN t_rating t2 ON t1.movieid = t2.movieid
         LEFT JOIN t_user t3 ON t2.userid = t3.userid
WHERE t3.sex = 'M'
GROUP BY t1.moviename HAVING total > 50
ORDER BY avgrate DESC
    LIMIT 10
```

![img2](https://github.com/eazonshaw/bigdata/blob/master/hive/hive_sql/img/img2.png)

## 题目三
* 困难：找出影评次数最多的女士所给出最高分的10部电影的平均影评分，展示电影名和平均影评分

```sql
SELECT t4.moviename moviename, t2.avgrate avgrate, t1.rate rate
FROM t_rating t1
         LEFT JOIN
     (
         SELECT t1_1.movieid, avg(t1_1.rate) avgrate
         FROM t_rating t1_1
         GROUP BY t1_1.movieid
     ) t2 ON t1.movieid = t2.movieid
         INNER JOIN
     (
         SELECT userid, max(total) max_total
         FROM(
                 SELECT t2_1.userid, count(t2_1.times) total
                 FROM t_rating t2_1
                          LEFT JOIN t_user t2_2 ON t2_1.userid = t2_2.userid
                 WHERE t2_2.sex = 'F'
                 GROUP BY t2_1.userid
             ) t2_3
         GROUP BY t2_3.userid
     ) t3 ON t1.userid = t3.userid
         LEFT JOIN t_movie t4 ON t1.movieid = t4.movieid
ORDER BY rate DESC
    LIMIT 10
```

![img3](https://github.com/eazonshaw/bigdata/blob/master/hive/hive_sql/img/img3.png)
