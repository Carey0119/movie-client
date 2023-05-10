package com.myjavafx.movieclient.config;

import com.myjavafx.movieclient.utils.ExceptionWriter;
import com.myjavafx.movieclient.utils.SpringFXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.io.StringWriter;


@Configuration
public class AppJavaConfig {

    @Resource
    SpringFXMLLoader springFXMLLoader;

    /**
     * Useful when dumping stack trace to a string for logging.
     *
     * @return ExceptionWriter contains logging utility methods
     */
    @Bean
    @Scope("prototype")
    public ExceptionWriter exceptionWriter() {
        return new ExceptionWriter(new StringWriter());
    }


//    @Bean
//    @Lazy(value = true) //Stage only created after Spring context bootstap
//    public StageManager stageManager(Stage stage, GNDecorator decorator) throws IOException {
//        return new StageManager(springFXMLLoader, stage, decorator);
//    }

}
