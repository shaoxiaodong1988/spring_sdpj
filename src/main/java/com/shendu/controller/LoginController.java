package com.shendu.controller;

import com.shendu.base.BaseController;
import com.shendu.exception.BusinessException;
import com.shendu.model.User;
import com.shendu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huangxinguang on 2017/5/22 下午3:19.
 */
@Controller
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping(value = "/login.html",method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "/loginIn.do",method = RequestMethod.POST)
    public ModelAndView loginIn(@ModelAttribute("user") User user) {
        ModelAndView mav = getModelAndView();
        boolean success = userService.check(user.getUserName(),user.getPassword());
        if(success) {
            getSession().setAttribute("user",user.getUserName());
        }else {
            mav.addObject("errorMessage","用户名或密码错误");
            mav.setViewName("login");
            return mav;
        }
        mav.setViewName("redirect:index.html");
        return mav;
    }

    /**
     * 退出登陆
     * @return
     */
    @RequestMapping(value = "/loginOut.do",method = RequestMethod.POST)
    public ModelAndView loginOut() {
        ModelAndView mav = getModelAndView();
        getSession().removeAttribute("user");
        mav.setViewName("redirect:index.html");
        return mav;
    }
}
