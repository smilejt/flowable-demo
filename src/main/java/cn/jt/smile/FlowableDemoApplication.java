package cn.jt.smile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author admin
 */
@SpringBootApplication
public class FlowableDemoApplication {
    private static final Logger log = LoggerFactory.getLogger(FlowableDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FlowableDemoApplication.class, args);
        log.info("---------------------> FlowableDemoApplication Successful Start <---------------------");
    }

}
