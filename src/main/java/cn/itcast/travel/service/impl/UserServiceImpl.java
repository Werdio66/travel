package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.IUserDAO;
import cn.itcast.travel.dao.impl.UserDAOImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.IUserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements IUserService {
    // 依赖dao
    private IUserDAO userDAO = new UserDAOImpl();
    @Override
    public boolean register(User user) {
        // 根据名称在数据库查找用户是否存在
        User u = userDAO.findByUserName(user.getUsername());
        if (u == null){
            // 设置激活码
            user.setStatus("N");
            // 设置唯一激活码
            user.setCode(UuidUtil.getUuid());
            // 用户不存在，保存
            userDAO.save(user);
            // 发送的信息
            String context = "<a href = 'http://localhost/travel/activeServlet?code=" + user.getCode() + "'>激活</a>";
            System.out.println("发送的消息:" + context);
            // 激活邮件发送
            MailUtils.sendMail(user.getEmail(),context,"激活");
        }else {
            // 用户存在，返回false
            return false;
        }
        // 保存用户
        return true;
    }

    /**
     *  修改激活状态
     * @param code      激活码
     * @return          true、false
     */
    @Override
    public boolean active(String code) {
        // 获取对应激活码的用户
        User user = userDAO.findByCode(code);
        System.out.println("获取对应激活码的用户" + user);
        if (user != null){
           userDAO.updateStatus(user);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User login(User user) {
        return userDAO.findByUserNameAndPassword(user);
    }
}
