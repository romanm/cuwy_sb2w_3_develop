package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
/*
	@Autowired
	private SpringTemplateEngine templateEngine;

	@PostConstruct
	public void extension() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
//		FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setPrefix("/WEB-INF/templates");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(templateEngine.getTemplateResolvers().size());
		resolver.setCacheable(false);
		templateEngine.addTemplateResolver(resolver);
		System.out.println(templateEngine);
	}
 * */
}
