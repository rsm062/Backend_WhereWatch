package ad.project.backend_wherewatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BackendWhereWatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendWhereWatchApplication.class, args);
    }
}
