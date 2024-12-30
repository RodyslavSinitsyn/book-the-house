package bth.notificator.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "bth.notificator.mail")
@Data
public class MailProperties {
    private List<String> whiteList;
}
