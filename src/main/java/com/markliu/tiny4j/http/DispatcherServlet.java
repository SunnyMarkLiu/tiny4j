package com.markliu.tiny4j.http;

import com.markliu.tiny4j.config.ConfigHelper;
import com.markliu.tiny4j.ioc.IocContainer;
import com.markliu.tiny4j.ioc.IocContainerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>全局请求分发器，拦截了所有的请求，内部的处理逻辑大致是这样的：</p>
 *
 * <p>1. 获取请求相关信息（请求方法与请求 URL），封装为 Request 对象。</p>
 * <p>2. 根据 Request 从 HandlerMapping 中获取对应的 Controller（包括 Controller 类与 Controller 方法）。</p>
 * <P>3. 解析请求 URL 中的占位符，并根据真实的 URL 生成对应的 Controller 方法参数列表（Controller 方法参数的顺序与 URL 占位符的顺序相同）。</P>
 * <p>4. 根据反射创建 Controller 对象，并调用 Controller 方法，最终获取返回值（Result）。</p>
 * <p>5. 根据返回的类型决定返回的是试图还是 Json 数据</p>
 * <p>5'. 将返回值转换为 JSON 格式（或者 XML 格式，可根据 Controller 方法上的 @Response 注解来判断）。(未实现)</p>
 *
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :下午9:28
 */
public class DispatcherServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        // 初始化 IoC 容器
        IocContainerLoader.initIocContainer();

        // 过滤请求的静态资源

        ServletContext servletContext = servletConfig.getServletContext();

        // 注册处理 JSP 的 Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        // 处理静态资源的默认 Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取当前请求相关数据
        String requestMethod = request.getMethod();
        String requestPath = request.getPathInfo();

        // 从请求与响应映射的 HandlerMapping 中获取对应的处理对象 Handler
        Handler handler = HandlerMapping.getRequestHandler(requestPath, requestMethod);
        if (handler != null) {
            Class<?> controllerClass = handler.getResponseController();
            Method actionMethod = handler.getActionMethod();

            // 从 IoC 容器中获取对应的 Controller 实例
            Object controllerBean = IocContainer.getBeanByClass(controllerClass);
            try {

                // 获取请求参数，实现参数绑定
                Enumeration<String> requestParameterNames = request.getParameterNames();
                Map<String, Object> requestParamMap = new HashMap<String, Object>();
                if (requestParameterNames != null) {
                    while (requestParameterNames.hasMoreElements()) {
                        String paramName = requestParameterNames.nextElement();
                        String paramValue = request.getParameter(paramName);

                        requestParamMap.put(paramName, paramValue);
                    }
                }

                ActionMethodParam actionMethodParam = new ActionMethodParam(requestParamMap);

                // 构造处理器链 HandlerExecutionChain
                // 调用处理器链的方法，最终调用 Controller 的方法。
                Object result = actionMethod.invoke(controllerBean, actionMethodParam);
                if (result instanceof View) { // 如果返回的是视图页面
                    View returnView = (View) result;
                    // 视图的地址
                    String viewPath = returnView.getViewPath();
                    viewPath = ConfigHelper.getAppJspPath() + viewPath;
                    System.out.println("viewPath: " + viewPath);
                    request.getRequestDispatcher(viewPath).forward(request, response);

                } else if (result instanceof Data) { // 如果返回的是 Json 数据
                    Data data = (Data) result;
                    Object model = data.getModel();
                    JsonWriter.writeJson(response, model);
                }
            } catch (Exception e) {
                LOGGER.error("handler this request error, caused by invoking the controller's method erroe!", e);
            }
        }
    }
}
