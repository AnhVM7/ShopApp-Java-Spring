package com.ptit.shopapp.components;

import com.ptit.shopapp.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@RequiredArgsConstructor
@Component
public class LocalizationUtil {
  private final MessageSource messageSource;
  private final LocaleResolver localeResolver;

  public String getLocalizedMessage(String messageKey, Object... params){
    HttpServletRequest request = WebUtil.getCurrentRequest();
    Locale locale = localeResolver.resolveLocale(request);
    return messageSource.getMessage(messageKey, params, locale);
  }
}
