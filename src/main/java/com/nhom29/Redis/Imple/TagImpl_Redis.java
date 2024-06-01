package com.nhom29.Redis.Imple;

import com.nhom29.Model.ERD.Tag;
import com.nhom29.Redis.Inter.TagInter_Redis;
import com.nhom29.Repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagImpl_Redis implements TagInter_Redis {

    private final TagRepository tagRepo;
    private final RedisTemplate redisTemplate;
    @Override
    public List<Tag> getAllTag() {
        return (List<Tag>) redisTemplate.opsForValue().get("TAG");
    }

    @Override
    public boolean updateTag() {
        try{
            redisTemplate.opsForValue().set("TAG", tagRepo.findAll());
            return true;
        }catch ( Exception ex ){
            log.info("{}", ex.getMessage());
            return false;
        }
    }
}
