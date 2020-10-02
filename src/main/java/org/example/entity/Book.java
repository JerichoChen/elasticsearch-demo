package org.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @JSONField(serialize = false)
    private String id;
    private String title;
    private String author;
    @JSONField(name = "publish_date",format = "yyyy-MM-dd")
    private Date publishDate;
    private String desc;

}
