package eazon;


import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HBaseApplication {

    private static final String TABLE_NAME = "student_eazonshaw";

    public static void main(String... args) {

        List<String> columnList = new ArrayList<>();
        columnList.add("info");
        columnList.add("score");
        HBaseUtil.createTable(TABLE_NAME,columnList);
        //Tom
        HBaseUtil.putRow(TABLE_NAME, "Tom", "info",
                Arrays.asList(new Pair<>("student_id","20210000000001"),new Pair<>("class","1")));
        HBaseUtil.putRow(TABLE_NAME, "Tom", "score",
                Arrays.asList(new Pair<>("understanding","75"),new Pair<>("programing","82")));
        //Jerry
//        HBaseUtil.putRow(TABLE_NAME, "Jerry", "info", "student_id", "20210000000002");
//        HBaseUtil.putRow(TABLE_NAME, "Jerry", "info", "class", "1");
//        HBaseUtil.putRow(TABLE_NAME, "Jerry", "score", "understanding", "85");
//        HBaseUtil.putRow(TABLE_NAME, "Jerry", "score", "programing", "67");
//        //Jack
//        HBaseUtil.putRow(TABLE_NAME, "Jack", "info", "student_id", "20210000000003");
//        HBaseUtil.putRow(TABLE_NAME, "Jack", "info", "class", "2");
//        HBaseUtil.putRow(TABLE_NAME, "Jack", "score", "understanding", "80");
//        HBaseUtil.putRow(TABLE_NAME, "Jack", "score", "programing", "80");
//        //Rose
//        HBaseUtil.putRow(TABLE_NAME, "Rose", "info", "student_id", "20210000000004");
//        HBaseUtil.putRow(TABLE_NAME, "Rose", "info", "class", "2");
//        HBaseUtil.putRow(TABLE_NAME, "Rose", "score", "understanding", "60");
//        HBaseUtil.putRow(TABLE_NAME, "Rose", "score", "programing", "61");
//        //xiaoyizhen G20190343010243
//        HBaseUtil.putRow(TABLE_NAME, "xiaoyizhen", "info", "student_id", "G20190343010243");

        //查询 xiaoyizhen 的数据
        System.out.println(HBaseUtil.getRow(TABLE_NAME,"Tom"));

    }

}