package com.example.JewelerProgressReport;

import com.example.JewelerProgressReport.configuration.SettingProperties;
import com.example.JewelerProgressReport.jewelry.gold_price.GoldPriceClient;
import com.example.JewelerProgressReport.parser.EplClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;


@SpringBootApplication
@EnableFeignClients(clients = {GoldPriceClient.class, EplClient.class})
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableConfigurationProperties({SettingProperties.class})
public class JewelerProgressReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelerProgressReportApplication.class, args);
	}

}
