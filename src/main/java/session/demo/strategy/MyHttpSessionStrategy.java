package session.demo.strategy;

import org.springframework.session.Session;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyHttpSessionStrategy implements HttpSessionStrategy {

    private String headerName = "LION-AUTH-TOKEN";

    @Override
    public String getRequestedSessionId(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(this.headerName);
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader(this.headerName, session.getId());
    }

    @Override
    public void onInvalidateSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader(this.headerName, "");
    }
}
