package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.IUserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    /**
     * 注册
     */
    public void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 接收用户输入验证码
        String check = req.getParameter("check");

        // 获取session中的验证码
        String  checkcode_server = (String) req.getSession().getAttribute("CHECKCODE_SERVER");
        // 删除session中的验证码，保证验证码使用一次
        req.getSession().removeAttribute("CHECKCODE_SERVER");
        // 判断验证码是否正确
        if (!check.equalsIgnoreCase(checkcode_server)) {
            // 后端返回前端数据对象
            ResultInfo info = new ResultInfo();
            System.out.println("用户输入："+check);
            System.out.println("验证码："+checkcode_server);
            // 注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
            // 将info对象序列化为 json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);

            // 将json返回客户端
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(json);
            return;
        }
        System.out.println("--------------");
        // 接收请求参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        User user = new User();
        try {
            // 将获取的值封装成user对象
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // 调用service完成注册
        IUserService userService = new UserServiceImpl();
        // 是否注册成功
        boolean flag = userService.register(user);
        // 后端返回前端数据对象
        ResultInfo info = new ResultInfo();
        if (flag) {
            // 注册成功
            info.setFlag(true);
            System.out.println("注册成功！");
        } else {
            // 注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }

        // 将info对象序列化为 json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        // 将json返回客户端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }

    /**
     *  登录
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    /**
     *  查找session中的user
     */
    public void findUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object user = req.getSession().getAttribute("user");
        System.out.println("user.name = " + user);
        ObjectMapper mapper = new ObjectMapper();
        // 设置响应类型
        resp.setContentType("application/json;charset=utf-8");
        mapper.writeValue(resp.getOutputStream(),user);
    }

    /**
     *  邮箱的激活
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 接收激活码
        String code = req.getParameter("code");
        System.out.println("code = " + code);
        if (code != null){
            // 调用service完成激活
            IUserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);

            String msg = "";
            if (flag){
                // 激活成功
                msg = "<a href = 'login.html'>" + "请登录" + "</a>";
            }else {
                msg = "激活失败";
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(msg);
        }
    }

    /**
     *  退出登录
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 清空session
        req.getSession().invalidate();

        // 跳转到登录页面
        resp.sendRedirect("login.html");
    }
}
