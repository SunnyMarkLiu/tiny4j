package com.markliu.tiny4j.http;

import com.markliu.tiny4j.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * author:sunnymarkliu
 * date  :16-9-22
 * time  :上午11:18
 */
public class JsonWriter {

    public static void writeJson(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtil.toJson(object));
        writer.flush();
        writer.close();
    }
}
