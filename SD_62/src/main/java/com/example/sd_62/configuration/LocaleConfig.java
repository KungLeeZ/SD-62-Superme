package com.example.sd_62.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String languageHeader = request.getHeader("Accept-Language");

        if (!StringUtils.hasLength(languageHeader)) {
            return Locale.getDefault();
        }

        return Locale.lookup(
                Locale.LanguageRange.parse(languageHeader),
                List.of(new Locale("en"), new Locale("vi"))
        );
    }
}

