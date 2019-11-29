package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface IUserService {
    /**
     *  注册
     * @param user  注册的对象信息
     * @return  注册是否成功
     */
    boolean register(User user);

    boolean active(String code);

    User login(User user);
}
