package com.store.controller;

import com.store.domain.*;
import com.store.domain.security.UserRole;
import com.store.service.UserService;
import com.store.service.impl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;
    @RequestMapping("/user-list")
    public String userList(Model model) {
        List<UserRole> userRoleList = userService.getListUserRole();
        model.addAttribute("userRoleList", userRoleList);
        for (UserRole u: userRoleList
             ) {
            System.out.println(u.toString());
        }
        return "userList";
    }

    @RequestMapping("/new-user")
    public String addUser() {
        return "addUser";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String addUserPost(@ModelAttribute("user") User user, HttpServletRequest request) {
            userService.createUser(user.getUsername(), user.getEmail(), user.getPassword(),  Arrays.asList("ROLE_USER"));
        return "redirect:article-list";
    }

    @RequestMapping(value="/new-user1", method=RequestMethod.POST)
    public String newUserPost(@Valid @ModelAttribute("user") User user, BindingResult bindingResults,
                              @ModelAttribute("new-password") String password,
                              RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("email", user.getEmail());
        model.addAttribute("username", user.getUsername());
        boolean invalidFields = false;
        if (bindingResults.hasErrors()) {
            return "redirect:/new-user";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("usernameExists", true);
            invalidFields = true;
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("emailExists", true);
            invalidFields = true;
        }
        if (invalidFields) {
            return "redirect:/new-user";
        }
        user = userService.createUser(user.getUsername(), password,  user.getEmail(), Arrays.asList("ROLE_USER"));
        userSecurityService.authenticateUser(user.getUsername());
        return "redirect:user-list";
    }

    @RequestMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User u = userService.findById(id);
        model.addAttribute("user", u);
        System.out.println(u.toString());
        return "editUser";
    }
    @RequestMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:user-list";
    }


}
