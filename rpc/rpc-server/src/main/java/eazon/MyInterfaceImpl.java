package eazon;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyInterfaceImpl implements MyInterface{

    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    static {
        map.put("G20190343010243","肖一镇");
        map.put("20210000000000","null");
        map.put("20210123456789","心心");
    }

    @Override
    public int add(int number1, int number2) {
        System.out.printf("number1 = %d , number2 = %d", number1, number2);
        return number1 + number2;
    }

    @Override
    public String findName(String studentId) {
        return map.get(studentId).toString();
    }

    @Override
    public long getProtocolVersion(String s, long l) throws IOException {
        return MyInterface.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String s, long l, int i) throws IOException {
        return null;
    }
}
