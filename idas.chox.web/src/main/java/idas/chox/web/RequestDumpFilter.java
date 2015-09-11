package idas.chox.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class RequestDumpFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestDumpFilter.class);

    ;
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
            try {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
                BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
                BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);

                final StringBuilder logMessage = new StringBuilder("Request: ")
                        .append("[HTTP METHOD:")
                        .append(httpServletRequest.getMethod())
                        .append("] [PATH:")
                        .append(httpServletRequest.getServletPath())
                        .append("] [REQUEST PARAMETERS:")
                        .append(requestMap)
//                        .append("] [REQUEST BODY:")
//                        .append(bufferedRequest.getRequestBody())
                        .append("] [REMOTE ADDRESS:")
                        .append(httpServletRequest.getRemoteAddr())
                        .append("] [SESSION:")
                        .append(httpServletRequest.getSession(false) == null ? "no session" : httpServletRequest.getSession(false).getId())
                        .append("]");

                if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
                    LOG.info(logMessage.toString());
                } else {
                    LOG.trace(logMessage.toString());
                }
                chain.doFilter(bufferedRequest, bufferedResponse);
                bufferedResponse.flushBuffer();
                if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
                    LOG.trace("[RESPONSE: {}]", new String(bufferedResponse.getCopy()));
                }
            } catch (IOException | ServletException a) {
                LOG.error("Error thrown: {}", a.getMessage(), a);
            }
        } else {
                chain.doFilter(request, response);
        }

    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue = request.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }

    @Override
    public void destroy() {
    }

    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private ByteArrayInputStream bais = null;
        private ByteArrayOutputStream baos = null;
        private BufferedServletInputStream bsis = null;
        private byte[] buffer = null;

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            // Read InputStream and store its content in a buffer.
            InputStream is = req.getInputStream();
            this.baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int letti;
            while ((letti = is.read(buf)) > 0) {
                this.baos.write(buf, 0, letti);
            }
            this.buffer = this.baos.toByteArray();
        }

        @Override
        public ServletInputStream getInputStream() {
            this.bais = new ByteArrayInputStream(this.buffer);
            this.bsis = new BufferedServletInputStream(this.bais);
            return this.bsis;
        }

        String getRequestBody() throws IOException {
            StringBuilder inputBuffer;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()))) {
                String line;
                inputBuffer = new StringBuilder();
                do {
                    line = reader.readLine();
                    if (null != line) {
                        inputBuffer.append(line.trim());
                    }
                } while (line != null);
            }
            return inputBuffer.toString().trim();
        }

    }

    private static final class BufferedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }

    }

    public class BufferedResponseWrapper extends HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter writer;
        private ServletOutputStreamCopier copier;

        public BufferedResponseWrapper(HttpServletResponse response) throws IOException {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called on this response.");
            }

            if (outputStream == null) {
                outputStream = getResponse().getOutputStream();
                copier = new ServletOutputStreamCopier(outputStream);
            }

            return copier;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (outputStream != null) {
                copier.flush();
            }
        }

        public byte[] getCopy() {
            if (copier != null) {
                return copier.getCopy();
            } else {
                return new byte[0];
            }
        }

    }

    public class ServletOutputStreamCopier extends ServletOutputStream {

        private OutputStream outputStream;
        private ByteArrayOutputStream copy;

        public ServletOutputStreamCopier(OutputStream outputStream) {
            this.outputStream = outputStream;
            this.copy = new ByteArrayOutputStream(1024);
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            copy.write(b);
        }

        public byte[] getCopy() {
            return copy.toByteArray();
        }

    }
}
