package com.nhom29.Service.Inter;

import com.nhom29.Model.ERD.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TagInter {
    List<Tag> getAllTag();
    Tag saveTag(String tag, List<Tag> tags);
    Tag save(Tag tag);
    Tag getTagByName(String name);
    Page<Tag> getTagInPage( Integer page);
    Tag saveTagALl(Tag tag, List<Tag> tags);
    Page<Tag> phanTrangTag(int offset, int pageSize, String sort, String q);
}
