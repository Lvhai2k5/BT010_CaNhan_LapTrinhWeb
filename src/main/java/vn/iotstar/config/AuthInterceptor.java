package vn.iotstar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();

        // Nếu chưa login => chặn
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect(request.getContextPath() + "/login?denied=1");
            return false;
        }

        String role = (String) session.getAttribute("role");

        // Check quyền admin
        if (uri.startsWith("/admin") && !"ADMIN".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login?denied=1");
            return false;
        }

        // Check quyền user
        if (uri.startsWith("/user") && !"USER".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login?denied=1");
            return false;
        }

        return true;
    }
}
