package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.json.JacksonObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry 注册类作为参数
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                //.excludePathPatterns("/user/shop/status")
                //.excludePathPatterns("/user/category/**")
                //.excludePathPatterns("/user/dish/**")
                //.excludePathPatterns("/user/setmeal/**")
                .excludePathPatterns();
    }

    /**
     * 通过knife4j生成接口文档
     *
     * @return 返回OpenAPI对象
     */
    @Bean
    public OpenAPI publicApi(/*Environment environment*/) {
        return new OpenAPI()
                //.servers(serverList())
                .info(new Info()
                        .title("苍穹外卖项目接口文档")
                        //.extensions(Map.of("x-audience", "external-partner", "x-application-id", "APP-12345"))
                        .description("苍穹外卖项目接口文档")
                        .version("1.0")
                );
        //.addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write"))).security(securityList());
    }

    /**
     * 设置静态资源映射，可省去此方法，因为implements WebMvcConfigurer可以自动配置路径映射
     *
     * @param registry 注册类作为参数
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展Spring MVC框架的消息转换器，将日期类型从列表转换为时间戳
     * 容易导致Knife4j不能正常显示,要注意添加的位置
     *
     * @param converters the list of configured converters to be extended
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //WebMvcConfigurer.super.extendMessageConverters(converters);
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将其加入容器中，顺序为原来几个MappingJackson2HttpMessageConverter之前，既避免顺序太后无法生效，也避免顺序太前导致Knife4j异常
        converters.add(converters.size() - 3, converter);
    }

}
