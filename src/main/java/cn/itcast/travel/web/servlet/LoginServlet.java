package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.IUserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用户输入的信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        IUserService userService = new UserServiceImpl();
        // 调用业务方法，得到指定用户名和密码的对象
        User u  = userService.login(user);
        ResultInfo info = new ResultInfo();
        // 用户不存在
        if (u == null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误！");
            System.out.println("用户名或密码错误！");
        }
        // 用户存在，且已激活
        if (u != null && "Y".equals(u.getStatus())){
            info.setFlag(true);
            System.out.println("登录成功！");
            // 将当前用户存入session中
            req.getSession().setAttribute("user",u);
        }
        // 用户存在，但邮箱未激活
        if (u != null && !"Y".equals(u.getStatus())){
            System.out.println(u.getStatus());
            info.setFlag(false);
            info.setErrorMsg("您好，您的邮箱未激活！");
            System.out.println("您好，您的邮箱未激活！");
        }

        ObjectMapper mapper = new ObjectMapper();
        // 将info对象序列化为json类型
        String json = mapper.writeValueAsString(info);

        // 设置响应类型
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
