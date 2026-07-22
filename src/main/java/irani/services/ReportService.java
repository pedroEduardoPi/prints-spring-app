package irani.services;

import com.opencsv.bean.CsvToBeanBuilder;
import irani.DTO.PrintsByDateDTO;
import irani.DTO.PrintsByUnitDTO;
import irani.entities.PrintData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<PrintsByUnitDTO> getPrintsByUnit() throws Exception{

        try{
            List<PrintData> prints = readCsv();

            return prints.stream()
                    .collect(Collectors.groupingBy(
                            print -> getUnitByIp(print.getEnderecoDispositivo()),
                            Collectors.groupingBy(
                                    print -> print.getDataImpressao().toLocalDate(),
                                    Collectors.summingInt(PrintData::getTotalImpresso)
                            )
                    ))
                    .entrySet()
                    .stream()
                    .map(unitEntry -> new PrintsByUnitDTO(
                            unitEntry.getKey(),
                            unitEntry.getValue()
                                    .entrySet()
                                    .stream()
                                    .sorted(Map.Entry.comparingByKey())
                                    .map(dateEntry -> new PrintsByDateDTO(
                                            dateEntry.getKey(),
                                            dateEntry.getValue()
                                    ))
                                    .toList()
                    ))
                    .toList();
        }
        catch (Exception e) {
            throw new Exception("Failed to filter prints by unit: " + e.getMessage());
        }
    }

    private String getUnitByIp(String ip) {

        if (ip.startsWith("172.16.23.")) return "MTZ";
        if (ip.startsWith("172.17.24.")) return "PV";
        if (ip.startsWith("172.19.23.")) return "TCR";
        if (ip.startsWith("172.26.23.")) return "PC";
        if (ip.startsWith("172.27.23.")) return "PF";
        if (ip.startsWith("172.18.23.")) return "FLO";
        if (ip.startsWith("192.168.140.")) return "IP-ART";

        return "Unknown";
    }
}
