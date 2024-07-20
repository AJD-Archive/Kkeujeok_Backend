package shop.kkeujeok.kkeujeokbackend.global.filter;

import java.io.IOException;
import java.util.UUID;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class LogFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);
            // chain이 없으면 여기서 끝난다. 즉, 로그만 띄우고 컨트롤러까지 가지 않아서 백지만 나온다.
            // chain doFilter로 다시 호출해주면 controller로 넘어가서 정상적으로 페이지를 띄운다.
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
        }
    }
}
