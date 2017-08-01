package nl.trifork.bootbatchadmindemo;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.batch.admin.service.SimpleJobServiceFactoryBean;
import org.springframework.batch.admin.web.JobController;
import org.springframework.batch.admin.web.JobExecutionController;
import org.springframework.batch.admin.web.StepExecutionController;
import org.springframework.batch.admin.web.resources.DefaultResourceService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@ImportResource({ "classpath:META-INF/spring/batch/servlet/manager/manager-context.xml",
                  "classpath:META-INF/spring/batch/servlet/resources/resources-context.xml" })
@Import({ JobController.class, StepExecutionController.class, JobExecutionController.class, SpringBatchAdminConfig.FilesController.class })
public class SpringBatchAdminConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(autowire = Autowire.BY_TYPE)
    SimpleJobServiceFactoryBean simpleJobService() {
        return new SimpleJobServiceFactoryBean();
    }

    @Bean
    PropertiesFactoryBean defaultResources() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("/org/springframework/batch/admin/web/manager/html-resources.properties"));
        return bean;
    }

    @Bean
    PropertiesFactoryBean jsonResources() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("/org/springframework/batch/admin/web/manager/json-resources.properties"));
        return bean;
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("org/springframework/batch/admin/web/manager/html-resources",
                "org/springframework/batch/admin/web/manager/json-resources");
        return messageSource;
    }

    @Bean
    DefaultResourceService resourceService() {
        DefaultResourceService defaultResourceService = new DefaultResourceService();
        defaultResourceService.setServletPath("/batch/");
        return defaultResourceService;
    }

    @Controller
    static class FilesController {
        @GetMapping("/files/**")
        public String get() {
            return "standard";
        }
    }
}
