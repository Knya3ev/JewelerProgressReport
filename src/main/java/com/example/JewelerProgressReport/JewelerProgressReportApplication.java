package com.example.JewelerProgressReport;

import com.example.JewelerProgressReport.config.SettingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;


@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableConfigurationProperties({SettingProperties.class})
public class JewelerProgressReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelerProgressReportApplication.class, args);
	}

}
