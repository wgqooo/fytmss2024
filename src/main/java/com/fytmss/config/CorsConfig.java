package com.fytmss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wgq
 * @create 2024/5/17-周五 19:53
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     *
     * 有的项目按照如上配置允许跨域请求成功了，但有些项目却不生效？
     * 其实就是一个结论：有中断响应的过滤器在 CorsFilter 之前执行了，
     * 也就无法执行到 CorsFilter，自然 CorsConfiguration 中的配置形同虚设
     * allowedOriginPatterns 和 allowedOrigins都是配置跨域请求来源的
     * 前者支持通配符,后者不支持通配符,只能字符串,取其一种即可
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //配置当前项目哪些路径支持跨域。这是所有路径都支持跨域
                .allowedOriginPatterns("*")//设置允许跨域请求的域名 /*表示所有
                .allowCredentials(true) //是否允许Cookie
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS") //设置允许的请求方式
                .allowedHeaders("*") //设置允许的header属性 *所有
                .maxAge(36000); //设置跨域允许时间
    }

}
