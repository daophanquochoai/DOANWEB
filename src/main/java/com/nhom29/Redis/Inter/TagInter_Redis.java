package com.nhom29.Redis.Inter;

import com.nhom29.Model.ERD.Tag;

import java.util.List;

public interface TagInter_Redis {
    List<Tag> getAllTag();
    boolean updateTag();
}
