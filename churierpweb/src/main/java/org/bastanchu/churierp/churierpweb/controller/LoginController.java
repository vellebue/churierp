package org.bastanchu.churierp.churierpweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String doLogin(HttpServletRequest request){
        if (request.getParameter("error") != null) {
            request.setAttribute("error", "true");
        }
        return "login.html";
    }

    @GetMapping("/logout")
    public String doLogout(HttpSession session) {
        session.invalidate();
        return "logout.html";
    }
}
