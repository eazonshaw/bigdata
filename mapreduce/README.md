# 记第一次 mapreduce 程序调试
## 前言
之前或多或少接触过 mapreduce ，知道一些原理，不过这次是真正第一次调试 mapreduce 的程序。

## wordcount
这应该是每个学 hadoop 和 mapreduce 都会接触过的入门级程序了吧，程序的目的就是做**词频统计**。

首先参考 [官网的教程](https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html) ，在服务器上建立一个`WordCount.java`的文件，编写 mapreduce 程序。
```java
public class WordCount {
    /** 这里是map程序
     * 输入的程序格式为 
     * Hello World Bye World
     * Hello Hadoop Goodbye Hadoop
     */
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{
    
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            //循环赋值
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }
    //这里是reduce程序
    public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
        
        private IntWritable result = new IntWritable();
        
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            //这里就是对单词进行累加了
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
    //这里是主程序
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        //map程序
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        //reduce程序
        job.setReducerClass(IntSumReducer.class);
        //输出的key和value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //这个是我自己加的，为了让reduce出来的文件是一个
        job.setNumReduceTasks(1);
        //入参出参
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

编写好程序后，进行编译，打包。按照官网的教程，是在 linux 上进行编译打包的，注意设置环境变量。这里附一个 [链接](https://www.cnblogs.com/hanshuai/p/9604730.html) 自行查看 jdk 的安装路径，方便设置环境变量。
```shell
export JAVA_HOME=/usr/java/default
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar

```

编译打包后得到一个 wc.jar 文件。好啦，现在程序有啦，准备我们要调试的输入文件 file。 将其传到指定目录底下。
```text
Hello World Bye World
Hello Hadoop Goodbye Hadoop
```
```shell
hadoop fs -put file /user/student/xiaoyizhen/wordcount/input
```

接下来就是远程调试啦~这里最重要的指令就是 `hadoop jar`。
```shell
hadoop jar wc.jar WordCount /user/student/xiaoyizhen/wordcount/input /user/student/xiaoyizhen/wordcount/output
```

> 注意：这里的 output 文件夹不能存在，所以每次执行调试需要将 output 先删除。

最后就是输出啦，查看 output 下的文件。完成啦~~~
```shell
hadoop fs -cat /user/student/xiaoyizhen/wordcount/output/part-r-00000
```
```text
Bye     1
Goodbye 1
Hadoop  2
Hello   2
World   2
```

## FlowBean

接下来就是第二个程序啦，在 IDEA 上编写，打包，然后远程到 hadoop 进行调试。

（待补充~）

