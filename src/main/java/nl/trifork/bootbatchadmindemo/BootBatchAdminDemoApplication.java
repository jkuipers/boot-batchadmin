package nl.trifork.bootbatchadmindemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

// FreeMarker is only used in the Batch Admin child context
@SpringBootApplication(exclude = FreeMarkerAutoConfiguration.class)
@EnableBatchProcessing
public class BootBatchAdminDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootBatchAdminDemoApplication.class, args);
    }

    @Bean
    ServletRegistrationBean springBatchAdminDispatcherServlet() {
        AnnotationConfigWebApplicationContext childContext = new AnnotationConfigWebApplicationContext();
        childContext.register(SpringBatchAdminConfig.class);

        return new ServletRegistrationBean(new BatchAdminDispatcherServlet(childContext), "/batch/*");
    }

    /**
     * DispatcherServlet which doesn't use {@link ViewResolver} beans from its parent context to resolve view names.
     * This avoids the Batch Admin view names from being resolved by the auto-configured
     * {@link org.springframework.web.servlet.view.ContentNegotiatingViewResolver} in the parent context, which only
     * knows about other ViewResolvers from that same context, causing the wrong {@link View} to be resolved.
     */
    static class BatchAdminDispatcherServlet extends DispatcherServlet {

        private List<ViewResolver> viewResolvers;

        BatchAdminDispatcherServlet(WebApplicationContext webApplicationContext) {
            super(webApplicationContext);
        }

        @Override
        protected void initStrategies(ApplicationContext context) {
            super.initStrategies(context);
            this.viewResolvers = new ArrayList<>(context.getBeansOfType(ViewResolver.class, true, false).values());
            AnnotationAwareOrderComparator.sort(this.viewResolvers);
        }

        @Override
        protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale, HttpServletRequest request) throws Exception {
            for (ViewResolver viewResolver : this.viewResolvers) {
                View view = viewResolver.resolveViewName(viewName, locale);
                if (view != null) {
                    return view;
                }
            }
            return null;
        }
    }
}
