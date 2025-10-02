package vn.iotstar.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    // Trang chủ
    @GetMapping("/")
    public String index() {
        return "index"; // trỏ tới index.html
    }

    // Form login
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // Xử lý login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("role", user.getRole());
            session.setAttribute("user", user);

            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/home";
            } else {
                return "redirect:/user/home";
            }
        }
        model.addAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu!");
        return "login";
    }

    // Form register
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Xử lý register
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {

        // Nếu có lỗi validate
        if (result.hasErrors()) {
            return "register";
        }

        // Kiểm tra email phải là Gmail
        if (!user.getUsername().endsWith("@gmail.com")) {
            model.addAttribute("errorMessage", "Email phải là địa chỉ Gmail!");
            return "register";
        }

        // Kiểm tra mật khẩu ≥ 6 ký tự
        if (user.getPassword().length() < 6) {
            model.addAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "register";
        }

        // Kiểm tra username đã tồn tại chưa
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "Tài khoản đã tồn tại!");
            return "register";
        }

        // Lưu user mới
        user.setRole("USER");
        userRepo.save(user);

        return "redirect:/login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
