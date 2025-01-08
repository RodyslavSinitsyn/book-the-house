package bth.notificator.service;

import bth.notificator.props.MailProperties;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HtmlTemplateService {

    private final Configuration freemarkerConfig;
    private final MailProperties mailProperties;

    @SneakyThrows
    public String loadTemplate(String templateKey, Map<String, Object> templateVariables) {
        var templateName = mailProperties.getHtmlTemplates().get(templateKey);
        if (templateName == null) {
            throw new IllegalArgumentException("Template not found: " + templateKey);
        }
        var template = freemarkerConfig.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateVariables);
    }
}
