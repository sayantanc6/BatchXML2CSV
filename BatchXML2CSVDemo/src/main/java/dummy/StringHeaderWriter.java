package dummy;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.stereotype.Component;

@Component
public class StringHeaderWriter implements FlatFileHeaderCallback {
	
    private final String header;
    
    public StringHeaderWriter(String header) {
        this.header = header;
    }

	@Override
	public void writeHeader(Writer writer) throws IOException {
        writer.write(header);
	}
}
