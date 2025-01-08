package bth.notificator.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "bth.notificator.mail")
@Data
public class MailProperties {
    private String sender;
    private List<String> whiteList;
    private Map<String, String> htmlTemplates = new HashMap<>();
}
