package com.school.management.application.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GenericBeans {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setSkipNullEnabled(false)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
        mapper.addConverter(new AbstractConverter<ZonedDateTime, String>() {
            @Override
            protected String convert(ZonedDateTime zonedDateTime) {
                if (zonedDateTime == null)
                    return "";
                return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }
        });
        mapper.addConverter(new AbstractConverter<String, ZonedDateTime>() {
            @Override
            protected ZonedDateTime convert(String value) {
                if (value == null)
                    return null;
                return ZonedDateTime.parse(value);
            }
        });
        return mapper;
    }
    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
    /*
     * This must be a Bean as this is a memory monster, ~120MiB just to parse User Agents!
     */
//    @Bean
//    @Lazy
//    public UserAgentAnalyzer userAgentAnalyzer() {
//        return UserAgentAnalyzer.newBuilder()
//                .withCache(5000)
//                .withField("DeviceName")
//                .withField("AgentName")
//                .delayInitialization()
//                .build();
//    }

}
