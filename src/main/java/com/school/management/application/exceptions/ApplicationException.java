package com.school.management.application.exceptions;

import lombok.Getter;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationException extends RuntimeException {
    protected static final  ReloadableResourceBundleMessageSource exceptionMessageSource;
    protected Map<String, String> params = new HashMap<>();

    static {
        exceptionMessageSource = new ReloadableResourceBundleMessageSource();
        exceptionMessageSource.addBasenames("classpath:/templates");
        exceptionMessageSource.setDefaultEncoding("UTF-8");
    }

    public ApplicationException() { super(); }

    public ApplicationException(String messageKey) {
        super();
        this.messageKey = messageKey;
    }

    public ApplicationException(String messageKey, String code) {
        super();
        this.messageKey = messageKey;
        this.code = code;
    }


    public ApplicationException(String messageKey, String code, Integer status) {
        super();
        this.code = code;
        this.messageKey = messageKey;
        this.status = status;
    }

    @Getter
    protected String code = "500";
    @Getter
    protected Integer status = 500;
    @Getter
    protected String messageKey = "application_exception";


    @Override
    public String getMessage() {
        try {
            return exceptionMessageSource.getMessage(this.getMessageKey(), null, Locale.ENGLISH);
        } catch (NoSuchMessageException e) {
            return this.getMessageKey();
        }
    }

    public Map<String, String> getParams() { return this.params; }

    public ApplicationException withParam(String key, String value) {
        this.params.put(key,value);
        return this;
    }

    @Override
    public String getLocalizedMessage() {
        try {
            return exceptionMessageSource.getMessage(this.getMessageKey(), null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return this.getMessageKey();
        }
    }

}
