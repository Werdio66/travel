package cn.itcast.travel.test;

import cn.itcast.travel.dao.IUserDAO;
import cn.itcast.travel.dao.impl.UserDAOImpl;
import cn.itcast.travel.domain.User;
import org.junit.jupiter.api.Test;

public class UserTest {
    private IUserDAO userDAO = new UserDAOImpl();
    @Test
    void testSave(){
        User user = new User();
        System.out.println(userDAO.findByUserName("user"));
    }

}
