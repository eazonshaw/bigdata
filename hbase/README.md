# 作业
作业：编程实践，使用 Java API 操作 HBase
主要实践建表、插入数据、删除数据、查询等功能。要求建立一个如下所示的表：

* 表名：$your_name:student
* 空白处自行填写, 姓名学号一律填写真实姓名和学号

![](https://static001.infoq.cn/resource/image/21/89/21ceb17dda135b92d718a5db94603689.png)

* 服务器版本为 2.1.0（hbase 版本和服务器上的版本可以不一致，但尽量保证一致）
```xml
<dependency>
    <groupId>org.apache.hbase</groupId>
    <artifactId>hbase-client</artifactId>
    <version>2.1.0</version>
</dependency>
```

# HBase 术语
* Table：Hbase的table由多个行组成
* Row：一个行在Hbase中由一个或多个有值的列组成。Row按照字母进行排序，因此行健的设计非常重要。这种设计方式可以让有关系的行非常的近，通常行健的设计是网站的域名反转，比如(org.apache.www, org.apache.mail, org.apache.jira)，这样的话所有的Apache的域名就很接近。
* Column：列由列簇加上列的标识组成，一般是“列簇：列标识”，创建表的时候不用指定列标识
* Column Family：列簇在物理上包含了许多的列与列的值，每个列簇都有一些存储的属性可配置。例如是否使用缓存，压缩类型，存储版本数等。在表中，每一行都有相同的列簇，尽管有些列簇什么东西也没有存。
* Column Qualifier：列簇的限定词，理解为列的唯一标识。但是列标识是可以改变的，因此每一行可能有不同的列标识
* Cell：Cell是由row，column family,column qualifier包含时间戳与值组成的，一般表达某个值的版本
* Timestamp：时间戳一般写在value的旁边，代表某个值的版本号，默认的时间戳是你写入数据的那一刻，但是你也可以在写入数据的时候指定不同的时间戳

# HBase JAVA API
## 建立连接
```java
Configuration configuration = HBaseConfiguration.create();
configuration.set("hbase.zookeeper.quorum","47.101.216.12");
connection = ConnectionFactory.createConnection(configuration);
```

> 这里遇到个问题，连接没有反应，排查发现是host解析的问题，在windows下`C:\Windows\System32\drivers\etc`配置hosts。

```
47.101.206.249    jikehadoop01
47.101.216.12     jikehadoop02
47.101.204.23     jikehadoop03
47.101.202.85     jikehadoop04
47.101.72.185     jikehadoop05
139.196.15.153    jikehadoop06
106.15.39.86      jikehadoop07
139.196.162.22    jikehadoop08
```

## 创建表
* 建表api
```java
/**
 * 创建HBase表
 *
 * @param tableName      表名
 * @param columnFamilies 列族的数组
 */
public static boolean createTable(String tableName, List<String> columnFamilies) {
    try {
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        System.out.println("creating table...");
        //判断表是否存在
        if (admin.tableExists(TableName.valueOf(tableName))) {
            return false;
        }
        //建表
        TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
        //循环设列簇
        columnFamilies.forEach(columnFamily -> {
            ColumnFamilyDescriptorBuilder cfDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
            cfDescriptorBuilder.setMaxVersions(1);
            ColumnFamilyDescriptor familyDescriptor = cfDescriptorBuilder.build();
            tableDescriptor.setColumnFamily(familyDescriptor);
        });
        admin.createTable(tableDescriptor.build());
        System.out.println("done!");
    } catch (IOException e) {
        e.printStackTrace();
    }
    return true;
}
```
* 调用
```java
List<String> columnList = new ArrayList<>();
columnList.add("info");
columnList.add("score");
HBaseUtil.createTable(TABLE_NAME,columnList);
```

* 创建完表之后，在 hbase 的 shell 可以看到建表成功：

![img1](https://github.com/eazonshaw/bigdata/blob/master/hbase/img/img1.png)

## 插入数据
* 按列族插入数据
```java
/**
 * 插入数据
 *
 * @param tableName        表名
 * @param rowKey           唯一标识
 * @param columnFamilyName 列族名
 * @param pairList         列标识和值的集合
 */
public static boolean putRow(String tableName, String rowKey, String columnFamilyName, List<Pair<String, String>> pairList) {
    try {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        pairList.forEach(pair -> put.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(pair.getKey()), Bytes.toBytes(pair.getValue())));
        table.put(put);
        table.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return true;
}
```
* 调用
```java
HBaseUtil.putRow(TABLE_NAME, "Tom", "info",
                Arrays.asList(new Pair<>("student_id","20210000000001"),new Pair<>("class","1")));
HBaseUtil.putRow(TABLE_NAME, "Tom", "score",
        Arrays.asList(new Pair<>("understanding","75"),new Pair<>("programing","82")));
```
* 插入数据完成后，在 hbase 的 shell 可以看到插入后的数据：
![img2](https://github.com/eazonshaw/bigdata/blob/master/hbase/img/img2.png)

## 查询数据
* 根据rowKey获取指定行的数据
```java
/**
 * 根据rowKey获取指定行的数据
 *
 * @param tableName 表名
 * @param rowKey    唯一标识
 */
public static Result getRow(String tableName, String rowKey) {
    try {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        return table.get(get);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
```
* 调用
```java
HBaseUtil.getRow(TABLE_NAME,"Tom")
```
* 控制台输出
```text
keyvalues={Tom/info:class/1627823941550/Put/vlen=1/seqid=0, Tom/info:student_id/1627823941550/Put/vlen=14/seqid=0, Tom/score:programing/1627823941597/Put/vlen=2/seqid=0, Tom/score:understanding/1627823941597/Put/vlen=2/seqid=0}
```
## 完整数据展示
![img3](https://github.com/eazonshaw/bigdata/blob/master/hbase/img/img3.png)

# 参考
* [入门 HBase ](https://www.jianshu.com/p/b23800d9b227)
* [HBase Shell常用命令和基本操作](http://c.biancheng.net/view/3587.html)
