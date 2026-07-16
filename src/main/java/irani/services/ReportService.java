package irani.services;

import com.opencsv.bean.CsvToBeanBuilder;
import irani.entities.PrintData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {

    public List<PrintData> readCsv() throws Exception {

        ClassPathResource resource =
                new ClassPathResource("assets/PjaExport-completo.csv");

        try (Reader reader = new InputStreamReader(
                resource.getInputStream(),
                StandardCharsets.UTF_8)) {

            List<PrintData> prints = new CsvToBeanBuilder<PrintData>(reader)
                    .withType(PrintData.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            prints.sort(Comparator.comparing(PrintData::getDataImpressao));

            return prints;

        }
        catch(Exception e) {
            throw new Exception("Failed to read CSV file+: " + e.getMessage());
        }
    }
}
