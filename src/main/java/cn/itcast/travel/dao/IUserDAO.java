package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface IUserDAO {
    /**
     *  根据名称查找user对象
     * @param username      名称
     * @return              user对象
     */
    User findByUserName(String username);

    /**
     *  保存用户
     * @param user      用户对象
     */
    void save(User user);

    /**
     *  根据激活码查找用户
     * @param code      激活码
     * @return          用户对象
     */
    User findByCode(String code);

    /**
     *  修改用户的激活状态
     * @param user      要修改的对象
     */
    void updateStatus(User user);
}
