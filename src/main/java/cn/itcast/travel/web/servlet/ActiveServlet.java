package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.IUserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeServlet")
public class ActiveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
