package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.IUserDAO;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDAOImpl implements IUserDAO {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUserName(String username) {
        String sql = "SETECT * FROM tab_user WHERE username = ?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        }catch (Exception e){
        }

        return user;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO tab_user (username,password,name,birthday,sex,telephone,email,status,code) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getName(),
                user.getBirthday(),user.getSex(),user.getTelephone(),user.getEmail(),
                user.getStatus(),user.getCode());
    }

    @Override
    public User findByCode(String code) {
        String sql = "SELECT * FROM tab_user WHERE code = ?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        }catch (Exception e){
        }

        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql = "UPDATE tab_user SET status = 'Y' WHERE uid = ?";
        jdbcTemplate.update(sql,user.getUid());
    }

    @Override
    public User findByUserNameAndPassword(User user) {
        String sql = "SELECT * FROM tab_user WHERE username = ? AND password = ?";
        User u = null;
        try{
            u = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),user.getUsername(),user.getPassword() );
        }catch (Exception e){
        }

        return u;
    }
}
