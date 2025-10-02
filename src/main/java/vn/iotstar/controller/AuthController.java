package vn.iotstar.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Hiển thị form login + gom mọi nguồn message về 1 attr "error"
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String errorParam,
                            @RequestParam(value = "denied", required = false) String deniedParam,
                            @RequestParam(value = "logout", required = false) String logoutParam,
                            @ModelAttribute("error") String errorFlash,
                            @ModelAttribute("info") String infoFlash,
                            Model model) {
        if (errorParam != null && !model.containsAttribute("error")) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
        }
        if (deniedParam != null && !model.containsAttribute("error")) {
            model.addAttribute("error", "Bạn không có quyền truy cập, hãy đăng nhập đúng tài khoản.");
        }
        if (logoutParam != null && !model.containsAttribute("info")) {
            model.addAttribute("info", "Bạn đã đăng xuất.");
        }
        // errorFlash / infoFlash từ Flash Attribute đã sẵn trong model nếu có
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null) {
            // Dùng Flash Attribute – chắc chắn hiển thị được sau redirect
            ra.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "redirect:/login";
        }

        session.setAttribute("role", user.getRole());
        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/home";
        } else {
            return "redirect:/user/home";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "register";
        }
        // mặc định role USER
        user.setRole("USER");
        try {
            userRepo.save(user);
            ra.addFlashAttribute("info", "Đăng ký thành công. Mời bạn đăng nhập.");
            return "redirect:/login";
        } catch (Exception ex) {
            // ví dụ trùng username (UK_users_username)
            ra.addFlashAttribute("error", "Tài khoản đã tồn tại.");
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("info", "Đã đăng xuất.");
        return "redirect:/login";
    }
}
