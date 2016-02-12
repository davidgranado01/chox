package idas.chox.emailnotification.config;

import idas.chox.emailnotification.util.EmailHelper;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("idas.chox.emailnotification")
public class EmailNotificationConfig {

    private @Value("${smtpHostName}") String smtpHostName;
    private @Value("${smtpPort}") String smtpPort;
    private @Value("${smtpEmailUser}") String smtpEmailUser;
    private @Value("${smtpEmailPassword}") String smtpEmailUserPassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EmailHelper emailHelper() {
        EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailUserPassword);
        return emailHelper;
    }

    @Bean
    public VelocityEngineFactoryBean velocityEngine(Properties velocityProperties) {
        VelocityEngineFactoryBean velocityEngine = new VelocityEngineFactoryBean();
        velocityEngine.setVelocityProperties(velocityProperties);
        return velocityEngine;
    }

    @Bean
    public Properties velocityProperties() {
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("resource.loader", "class");
        velocityProperties.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
        velocityProperties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityProperties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        return velocityProperties;
    }

}
