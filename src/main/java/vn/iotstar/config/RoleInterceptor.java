package vn.iotstar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String uri = request.getRequestURI();

        if (uri.startsWith("/admin") && !"ADMIN".equals(role)) {
            response.sendRedirect("/login?error=unauthorized");
            return false;
        }
        if (uri.startsWith("/user") && !"USER".equals(role)) {
            response.sendRedirect("/login?error=unauthorized");
            return false;
        }
        return true;
    }
}
