package dummy.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import dummy.StringHeaderWriter;
import dummy.Student;
import dummy.StudentProcessor;
import dummy.SudentJobListener;

@Configuration 
public class BatchConfiguration {
	
    private static final String PROPERTY_CSV_EXPORT_FILE_HEADER = "batch.job.export.file.header";
    private static final String PROPERTY_CSV_EXPORT_FILE_PATH = "file.output";


	  @Autowired
	  private JobBuilderFactory jobBuilderFactory;
	
	  @Autowired
	  private StepBuilderFactory stepBuilderFactory;
	   
	  @Value("${file.input}") 
	  private String fileInput;
	  
	  @Autowired
	  Environment environment;
	 
	  @Bean
	  public Job CsvToXmlJob() {
	    return jobBuilderFactory.get("XmlToCsvJob")
	    		.incrementer(new RunIdIncrementer()).listener(listener())
	    		.flow(step1())
	    		.end().build();
	  }
	 
	  @Bean
	  public Step step1() {
	    return stepBuilderFactory.get("step1").<Student, Student>chunk(10).reader(reader())
	        .writer(writer()).processor(processor()).build();
	  }
	 
	  @Bean
	  public StudentProcessor processor() {
	    return new StudentProcessor();
	  }
	  
	  @Bean
	  public StaxEventItemReader<Student> reader() {
		  Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
	        studentMarshaller.setClassesToBeBound(Student.class); 

	        return new StaxEventItemReaderBuilder<Student>() 
	                .name("studentReader")
	                .resource(new ClassPathResource(fileInput))
	                .addFragmentRootElements("student")
	                .unmarshaller(studentMarshaller)
	                .build();
	  }
	 
	  @Bean
	  public FlatFileItemWriter<Student> writer() { 
		  String exportFilePath = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_PATH); 
	        Resource exportFileResource = new FileSystemResource(exportFilePath);
	        
		  String exportFileHeader = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_HEADER);
	        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
	        
		  return new FlatFileItemWriterBuilder<Student>()
	                .name("studentWriter")
	                .headerCallback(headerWriter)
	                .lineAggregator(createStudentLineAggregator())
	                .resource(exportFileResource)
	                .build();
	  }
	  
	  private LineAggregator<Student> createStudentLineAggregator() {
	        DelimitedLineAggregator<Student> lineAggregator = new DelimitedLineAggregator<>();
	        lineAggregator.setDelimiter(";");

	        FieldExtractor<Student> fieldExtractor = createStudentFieldExtractor();
	        lineAggregator.setFieldExtractor(fieldExtractor);

	        return lineAggregator;
	    }

	    private FieldExtractor<Student> createStudentFieldExtractor() {
	        BeanWrapperFieldExtractor<Student> extractor = new BeanWrapperFieldExtractor<>();
	        extractor.setNames(new String[] {"name", "emailAddress", "purchasedPackage"});
	        return extractor;
	    }
	 
	@Bean
	  public Jaxb2Marshaller studentUnmarshaller() {
		  Jaxb2Marshaller unMarshaller = new Jaxb2Marshaller();
	    unMarshaller.setClassesToBeBound(Student.class); 
	    return unMarshaller;
	  }
	
	@Bean
	public JobExecutionListener listener() {
		return new SudentJobListener();
	}
}
