package com.kwewk.course_project.config.db;

import com.kwewk.course_project.model.User;
import com.kwewk.course_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private static final String USER_ID_HEADER = "User-Id";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        try {
            String userIdHeader = request.getHeader(USER_ID_HEADER);

            if (userIdHeader != null && !userIdHeader.isEmpty()) {
                Long userId = Long.parseLong(userIdHeader);
                User user = userRepository.findById(userId).orElse(null);

                if (user != null && user.getIsRegistered()) {
                    DataSourceContextHolder.set("main_user");
                    log.debug("Set DataSource to: main_user (User ID: {})", userId);
                } else {
                    DataSourceContextHolder.set("guest");
                    log.debug("Set DataSource to: guest");
                }
            } else {
                DataSourceContextHolder.set("guest");
                log.debug("No User-Id header, using guest access");
            }

            return true;

        } catch (Exception e) {
            log.error("Error in DataSourceInterceptor", e);
            DataSourceContextHolder.set("guest");
            return true;
        }
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        DataSourceContextHolder.clear();
    }
}