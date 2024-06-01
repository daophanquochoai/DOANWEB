package com.nhom29.Service.Inter;

public interface FollowInter {
    void follow( Long baidangId, Long thongtinId);
    void unfollow(Long baidangId, Long thongtinId);
    void like( Long baidangId, Long thongtinId);
    void unlike(Long baidangId, Long thongtinId);
}
