package org.example.sms.data;

import org.example.entity.SmsLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MockDataUtil {
    private static String[] corpNames = new String[]{"途虎", "淘宝", "阿里巴巴", "杭州银行", "百度"};
    private static String[] ipAddrs = new String[]{"127.0.0.1", "198.12.20.11", "128.78.0.12", "118.22.40.32"};
    private static String[] provinces = new String[]{"河南", "浙江", "上海", "湖南", "北京"};
    private static String[] mobiles = new String[]{"13500002222", "13800001111", "13944442222", "18800001234", "15812345678", "15500004321"};

    public static String getRandom(String[] arr) {
        int index = Double.valueOf(Math.floor(Math.random() * arr.length)).intValue();
        return arr[index];
    }

    public static String getRandomContent() {
        String content = "阿里巴巴西溪园区又名淘宝城是知名互联网公司阿里巴巴集团的总部所在地坐落于杭州市余杭区文一西路969号高教路口杭州未来科技城核心区域五常街道阿里巴巴西溪园区东至常二路南至创新路西至高教路北至文一西路规划总占地26万平方米一期项目由8幢单体建筑和2幢停车楼组成包括了员工主办公区食堂健身房报告厅和影视放映厅等日常办公休闲的基础设施总建筑面积约29万平方米[1]其主建筑体的设计由日本设计大师隈研吾担纲";
        Random random = new Random();
        int beginIndex = random.nextInt(content.length() - 10);
        int endIndex = beginIndex + 5 + random.nextInt(5);
        return content.substring(beginIndex, endIndex);
    }

    public static long getRandomFee() {
        return new Random().nextInt(100);
    }

    public static List<SmsLog> mock() {
        List<SmsLog> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SmsLog smsLog = new SmsLog();
            smsLog.setId((long) i);
            smsLog.setSmsContent(getRandomContent());
            smsLog.setCorpName(getRandom(corpNames));
            smsLog.setIpAddr(getRandom(ipAddrs));
            smsLog.setProvince(getRandom(provinces));
            smsLog.setMobile(getRandom(mobiles));
            smsLog.setFee(getRandomFee());
            smsLog.setCreateDate(new Date());
            list.add(smsLog);
            System.out.println(smsLog);
        }
        return list;
    }
}


