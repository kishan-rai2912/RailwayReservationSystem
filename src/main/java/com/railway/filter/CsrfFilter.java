
package com.railway.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class CsrfFilter implements Filter {

    private static final String CSRF_SESSION_KEY = "csrf";
    private static final String CSRF_REQUEST_KEY = "csrfToken";

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void init(FilterConfig config) {
        // No special initialization required.
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String getOrCreateToken(HttpSession session) {
        synchronized (session) {
            String token = (String) session.getAttribute(
                    CSRF_SESSION_KEY);

            if (token == null || token.isBlank()) {
                token = generateToken();
                session.setAttribute(CSRF_SESSION_KEY, token);
            }

            return token;
        }
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Do not allow browser caches to retain token-bearing forms.
        resp.setHeader("Cache-Control", "no-store");
        resp.setHeader("Pragma", "no-cache");

        HttpSession session = req.getSession(true);
        String expectedToken = getOrCreateToken(session);

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String suppliedToken = req.getParameter("csrf");

            boolean valid = suppliedToken != null
                    && !suppliedToken.isBlank()
                    && MessageDigest.isEqual(
                            expectedToken.getBytes(StandardCharsets.UTF_8),
                            suppliedToken.getBytes(StandardCharsets.UTF_8));

            if (!valid) {
                resp.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Invalid CSRF token. Refresh the page and try again.");
                return;
            }
        }

        req.setAttribute(CSRF_REQUEST_KEY, expectedToken);

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // Nothing to release.
    }
}
