package eazon;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ClientApplication {

    public static void main(String[] args) {
        try {
            MyInterface proxy = RPC.getProxy(MyInterface.class, MyInterface.versionID,
                    new InetSocketAddress("127.0.0.1", 12345), new Configuration());
            int res = proxy.add(1, 2);
            System.out.println(res);
            //输出学号，输出姓名
            System.out.println("学号：G20190343010243，姓名：" + proxy.findName("G20190343010243"));
            System.out.println("学号：20210000000000，姓名：" + proxy.findName("20210000000000"));
            System.out.println("学号：20210123456789，姓名：" + proxy.findName("20210123456789"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
