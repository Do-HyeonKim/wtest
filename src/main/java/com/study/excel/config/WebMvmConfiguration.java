package com.study.excel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvmConfiguration implements WebMvcConfigurer {

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		 registry.addInterceptor(new LoggerInterceptor())
//         .excludePathPatterns("/css/**", "/images/**", "/js/**");
//	}
//	
//	@Bean
//	public CommonsMultipartResolver multipartResolver() {
//		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//		commonsMultipartResolver.setDefaultEncoding("UTF-8");
//		commonsMultipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024);
//		return commonsMultipartResolver;
//	}

}
