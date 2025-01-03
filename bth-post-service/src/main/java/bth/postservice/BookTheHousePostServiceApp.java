package bth.postservice;

import bth.common.rabbitmq.RabbitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "bth.*")
@EnableAsync
@Import(RabbitConfig.class)
@EnableElasticsearchRepositories("bth.postservice.repo.elastic")
@EnableJpaRepositories("bth.postservice.repo.jpa")
public class BookTheHousePostServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(BookTheHousePostServiceApp.class, args);
    }

}
