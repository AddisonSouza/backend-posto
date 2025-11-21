package br.com.jaas.backendposto.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ServletHelper() {
    }

    public static <T> T parseRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return objectMapper.readValue(sb.toString(), clazz);
    }

    public static <T> void writeJsonResponse(HttpServletResponse response, int statusCode, T data) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    public static Long getIdFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String id = pathInfo.substring(1);
            try {
                return Long.parseLong(id);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
