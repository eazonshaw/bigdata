package eazon.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class FlowBeanMapper  extends Mapper<LongWritable,Text, Text,FlowBean> {
    private final Text outK =   new Text();
    private final FlowBean outV =   new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1.获取和一行信息
        String line = value.toString();

        //2.切割
        //时间戳、            电话号码、         基站的物理地址、       访问网址的ip、         网站域名、  数据包、接包数、上行/传流量、下行/载流量、响应码
        // ﻿1363157985066 	13726230503	00-FD-07-A4-72-B8:CMCC	120.196.100.82	i02.c.aliimg.com 24	   27	2481	   24681	200
        String[] split = line.split("\t");

        //3.抓取数据
        String phoneno = split[1];
        String upflow = split[split.length-3];
        String downflow = split[split.length-2];

        //4.封装
        outK.set(phoneno);
        outV.setUpFlow(Long.parseLong(upflow));
        outV.setDownFlow(Long.parseLong(downflow));
        outV.setSumFlow();

        //5.写出
        context.write(outK,outV);
    }
}
