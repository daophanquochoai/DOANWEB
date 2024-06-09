package com.nhom29.Service.Impl;

import com.nhom29.Model.ERD.Tag;
import com.nhom29.Redis.Inter.TagInter_Redis;
import com.nhom29.Repository.TagRepository;
import com.nhom29.Service.Inter.TagInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService implements TagInter {
    // contructor repo
    private final TagRepository tagRepo;
    private final RedisTemplate redisTemplate;
    private final TagInter_Redis tagInterRedis;
    @Override
    public List<Tag> getAllTag() {
        System.out.println("=======================");
        System.out.println("USING DB - tag all");
        System.out.println("=======================");
        return tagRepo.findAll();
    }

    @Override
    public Tag saveTag(String tag, List<Tag> tags) {
        try{
            Optional<Tag> t = tags.stream().filter( temp -> temp.getTenTag().trim().equals(tag)).findFirst();
            if( t.isPresent() ) return t.get();
            else{
                Tag tagNew = new Tag();
                tagNew.setTenTag(tag);
                Tag tagAfter = tagRepo.save(tagNew);
                redisTemplate.opsForValue().set("COMMON", tagRepo.findAll());
                return tagAfter;
                }
            } catch (Exception e) {
            log.info("{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Tag save(Tag tag) {
        Tag tagAfter = tagRepo.save(tag);
        redisTemplate.opsForValue().set("COMMON", tagRepo.findAll());
        return tagAfter;
    }

    @Override
    public Tag getTagByName(String name) {
        Optional<Tag> tag = tagRepo.findTagByName(name);
        if( tag.isEmpty()) return null;
        return tag.get();
    }

    @Override
    public Page<Tag> getTagInPage(Integer page) {
        return tagRepo.findAll(PageRequest.of(page,6).withSort(Sort.by("thoigiantao")));
    }

    @Override
    public Tag saveTagALl(Tag tag, List<Tag> tags) {
        try {
            if(tag.getTenTag().isEmpty() || tag.getNoidung().isEmpty())
            {
                throw new RuntimeException("tieu de va noi dung khong duoc trong!");
            }
            String tagName = tag.getTenTag().toLowerCase();
            Tag existTag = null;
            for (Tag item : tags) {
                if(item.getTenTag().toLowerCase().equals(tagName))
                {
                    existTag = item;
                    break;
                }
            }
            if(existTag != null)
            {
                existTag.setNoidung(tag.getNoidung());
                existTag.setThoigiantao(LocalDateTime.now());
                return tagRepo.save(existTag);
            }
            else
            {
                tag.setThoigiantao(LocalDateTime.now());
                return tagRepo.save(tag);
            }
        }
        catch (Exception ex)
        {
            log.warn(ex.getMessage());
            return null;
        }
    }

    @Override
    public Page<Tag> phanTrangTag(int offset, int pageSize, String sort,String q) {
        if(sort.equals("macdinh"))
        {
            return tagRepo.findByQ(q,PageRequest.of(offset, pageSize));
        }

        return tagRepo.findByQ(q,PageRequest.of(offset, pageSize).withSort(Sort.by(sort).descending()));
    }
}
