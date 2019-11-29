package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.IUserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 *  注册
 */
@WebServlet("/registerServlet")
public class RegisterSerlvet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
}
