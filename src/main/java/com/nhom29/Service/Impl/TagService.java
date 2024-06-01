package com.nhom29.Service.Impl;

import com.nhom29.Model.ERD.Tag;
import com.nhom29.Repository.TagRepository;
import com.nhom29.Service.Inter.TagInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService implements TagInter {
    // contructor repo
    private final TagRepository tagRepo;
    private final RedisTemplate redisTemplate;
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
}
