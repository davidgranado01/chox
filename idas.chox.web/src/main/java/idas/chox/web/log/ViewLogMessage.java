
package idas.chox.web.log;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.html.UrlCssBuilder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.read.CyclicBufferAppender;
import javax.servlet.http.HttpSession;

public class ViewLogMessage extends HttpServlet {

    private static final String USERID_MDC_KEY = "userid";
    private static final String CYCLIC_BUFFER_APPENDER_NAME = "CYCLIC";
    private static String USER_KEY = "ServletLogin.user";
    private static String FIELD_USER = "username";
    private static String FIELD_PASSWORD = "password";
    private static String FIELD_LOGOUT = "logout";
    private static String FIELD_LOGIN = "login";
    private String userName = "admin";
    private String password = "C0mpliance";
    private Logger logger = LoggerFactory.getLogger(ViewLogMessage.class);
    private CyclicBufferAppender<ILoggingEvent> cyclicBufferAppender;
    private HTMLLayout layout;
    private static String PATTERN = "%d%thread%level%logger{25}%mdc{"
            + USERID_MDC_KEY + "}%msg";

    @Override
    public void init() throws ServletException {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        initialize(lc);
        super.init();
    }

    void reacquireCBA() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        cyclicBufferAppender = (CyclicBufferAppender<ILoggingEvent>) context.getLogger(
                Logger.ROOT_LOGGER_NAME).getAppender(CYCLIC_BUFFER_APPENDER_NAME);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        resp.setContentType("text/html");
        java.io.PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(true);
        String user = (String) session.getAttribute(USER_KEY);
        String uri = req.getRequestURI();
        String logout = req.getParameter(FIELD_LOGOUT);
        String login = req.getParameter(FIELD_LOGIN);
        if (logout == null) {

            if (login == null && user != null) {

                reacquireCBA();

                out.append(layout.getFileHeader());
//            String localRef = req.getContextPath();
                out.append("<h2>CHOX Last log entries</h2>");
                out.append("<table class=\"nav\">");

                out.append("<tr><td class=\"sexy\"><a href=\"#bottom\" class=\"sexy\">Jump to bottom</a></td>"
                        + "<td align=\"right\" class=\"sexy\"><form method=POST action=\"" + uri + "\">"
                        + "<input type=\"submit\" name=\"logout\" value=\"Logout\"> </form></td></tr>");
                out.append("</table>");

                out.append("<div class=\"content_full\">");
                out.append(layout.getPresentationHeader());

                printLogs(out);

                out.append(layout.getPresentationFooter());

                out.append("<a name=\"bottom\" />");

                out.append("</div>");

                out.append(layout.getFileFooter());

                out.flush();
                out.close();

            } else {
                
                String usernameValue = req.getParameter(FIELD_USER);
                String passwordValue = req.getParameter(FIELD_PASSWORD);

                if (!validUser(usernameValue, passwordValue)) {
                    session.removeAttribute(USER_KEY);
                    out.println("<html>");
                    out.println("<title>Invalid User</title>");
                    out.println("<body><center><h2>" + "Invalid User!</h2><br>");
                    out.println("Press the 'Back' button to try again");
                    out.println("</center></body></html>");
                    out.flush();
                    return;
//                    login(out, uri);
                }
                session.setAttribute(USER_KEY, usernameValue);
                resp.sendRedirect(uri);
            }

        } else {
            session.removeAttribute(USER_KEY);
            resp.sendRedirect(uri);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            java.io.IOException {
        resp.setContentType("text/html");
        java.io.PrintWriter out = resp.getWriter();
        resp.setHeader("Expires", "Tues, 01 Jan 1980 00:00:00 GMT");
        String uri = req.getRequestURI();

        HttpSession session = req.getSession();
        String user = (String) session.getAttribute(USER_KEY);

        if (user == null) {
            login(out, uri);
            return;
        }
        doPost(req, resp);
    }

    private void printLogs(PrintWriter output) {
        int count = -1;
        if (cyclicBufferAppender != null) {
            count = cyclicBufferAppender.getLength();
        }

        if (count == -1) {
            output.append("<tr><td>Failed to locate CyclicBuffer</td></tr>\r\n");
        } else if (count == 0) {
            output.append("<tr><td>No logging events to display</td></tr>\r\n");
        } else {
            LoggingEvent le;
            for (int i = 0; i < count; i++) {
                le = (LoggingEvent) cyclicBufferAppender.get(i);
                output.append(layout.doLayout(le) + "\r\n");
            }
        }
    }

    private void initialize(LoggerContext context) {
        logger.debug("Initializing ViewLastLog Servlet");
        cyclicBufferAppender = (CyclicBufferAppender<ILoggingEvent>) context.getLogger(
                Logger.ROOT_LOGGER_NAME).getAppender(CYCLIC_BUFFER_APPENDER_NAME);

        layout = new HTMLLayout();
        layout.setContext(context);
        UrlCssBuilder cssBuilder = new UrlCssBuilder();
        cssBuilder.setUrl("../css/logBack.css");
        layout.setCssBuilder(cssBuilder);
        layout.setPattern(PATTERN);
        layout.setTitle("Last Logging Events");
        layout.start();
    }

    protected void login(java.io.PrintWriter out, String uri) throws java.io.IOException {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login</title>");
        out.println("<center><h2>Please login before accessing the CHOX log information</h2>");
        out.println("<br><form method=POST action=\"" + uri + "\">");
        out.println("<table>");
        out.println("<tr><td>User ID:</td>");
        out.println("<td><input type=text name=" + FIELD_USER + " size=30></td></tr>");
        out.println("<tr><td>Password:</td>");
        out.println("<td><input type=password name=" + FIELD_PASSWORD + " size=10></td></tr>");
        out.println("</table><br>");
        out.println("<input type=submit name=\"login\" value=\"Login\">");
        out.println("</form></center></body></html>");
    }

    protected boolean validUser(String username, String password) {

        if ((username != null) && (username.length() > 0) && (password != null) && (password.length() > 0)) {
            if (username.equals(this.userName) && password.equals(this.password)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public boolean isResetResistant() {
        return false;
    }

    public void onStop(LoggerContext arg0) {
    }
}
