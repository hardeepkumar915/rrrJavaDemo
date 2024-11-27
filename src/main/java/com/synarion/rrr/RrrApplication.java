package com.synarion.rrr;

import com.synarion.rrr.Service.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RrrApplication {

	public static void main(String[] args) {
		SpringApplication.run(RrrApplication.class, args);

		Utils utils = new Utils();
		System.out.println(utils.getCurrentTime());
	}

}
