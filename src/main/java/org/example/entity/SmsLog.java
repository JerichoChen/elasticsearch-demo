package org.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmsLog {
    public static String ID = "id";
    public static String CORP_NAME = "corpName";
    public static String CREATE_DATE = "createDate";
    public static String SMS_CONTENT = "smsContent";
    public static String IP_ADDR = "ipAddr";
    public static String FEE = "fee";
    public static String PROVINCE = "province";
    public static String MOBILE = "mobile";

    private Long id;
    @JSONField(format = "yyyy-MM-dd")
    private Date createDate = new Date();
    private String corpName;
    private String smsContent;
    private String province;
    private String ipAddr;
    private String mobile;
    private Long fee;
}