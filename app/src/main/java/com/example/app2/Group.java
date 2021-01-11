package com.example.app2;

import java.util.ArrayList;
import java.util.Date;

public class Group {
    public String groupId;
    public String startPoint;
    public String endPoint;
    public Date time;
    public int member_num;
    public String creator;

    public void Group(String groupid, String startpoint, String endpoint, Date time ,int member_num, String creator) {
        this.groupId = groupid;
        this.startPoint = startpoint;
        this.endPoint = endpoint;
        this.time = time;
        this.member_num = member_num;
        this.creator = creator;
    }

}
