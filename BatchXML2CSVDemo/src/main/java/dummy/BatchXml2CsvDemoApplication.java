package dummy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class BatchXml2CsvDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchXml2CsvDemoApplication.class, args);
	}

}
