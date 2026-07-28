package irani.services;

import com.opencsv.bean.CsvToBeanBuilder;
import irani.DTO.DepartmentReportDTO;
import irani.DTO.PrintByDepartmentDTO;
import irani.DTO.PrintsByDateDTO;
import irani.DTO.PrintsByUnitDTO;
import irani.entities.PrintData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public List<PrintsByUnitDTO> getPrintsByUnit() throws Exception {

        try {
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
                            fillMissingDays(unitEntry.getValue())
                    ))
                    .toList();

        } catch (Exception e) {
            throw new Exception("Failed to filter prints by unit: " + e.getMessage(), e);
        }
    }

    private List<PrintsByDateDTO> fillMissingDays(Map<LocalDate, Integer> printsByDate) {

        if (printsByDate.isEmpty()) {
            return List.of();
        }

        LocalDate firstDate = printsByDate.keySet()
                .stream()
                .min(LocalDate::compareTo)
                .orElseThrow();

        YearMonth yearMonth = YearMonth.from(firstDate);

        List<PrintsByDateDTO> result = new ArrayList<>();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {

            LocalDate date = yearMonth.atDay(day);

            result.add(new PrintsByDateDTO(
                    date,
                    printsByDate.getOrDefault(date, 0)
            ));
        }

        return result;
    }

    private String getUnitByIp(String ip) {

        Map<String, List<String>> units = Map.of(
                "MTZ", List.of("172.16.24.", "172.16.23."),
                "PV", List.of("172.17.24.", "172.17.23."),
                "TCR", List.of("172.19.23.", "172.19.24."),
                "PC", List.of("172.26.23.", "172.26.24."),
                "PF", List.of("172.27.23.", "172.27.24."),
                "FLO", List.of("172.18.23.", "172.18.24."),
                "IP-ART", List.of("192.168.140.", "192.168.140.")
        );



        return units.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .stream()
                        .anyMatch(ip::startsWith))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("Unknown");
    }

    private List<String> getIpsByUnit(String unit) {

        Map<String, List<String>> units = Map.of(
                "MTZ", List.of("172.16.24.", "172.16.23."),
                "PV", List.of("172.17.24.", "172.17.23."),
                "TCR", List.of("172.19.23.", "172.19.24."),
                "PC", List.of("172.26.23.", "172.26.24."),
                "PF", List.of("172.27.23.", "172.27.24."),
                "FLO", List.of("172.18.23.", "172.18.24."),
                "IP-ART", List.of("192.168.140.")
        );

        return units.getOrDefault(unit, List.of());
    }
//    private String getDepartament(String departament) {
//
//        if (departament.toUpperCase().contains("FRENTELOJA")) return "MTZ";
//        if (ip.startsWith("172.17.24.")) return "PV";
//        if (ip.startsWith("172.19.23.")) return "TCR";
//        if (ip.startsWith("172.26.23.")) return "PC";
//        if (ip.startsWith("172.27.23.")) return "PF";
//        if (ip.startsWith("172.18.23.")) return "FLO";
//        if (ip.startsWith("192.168.140.")) return "IP-ART";
//
//        return "Unknown";
//    }

    private String extractDepartment(String value) {

        Matcher matcher = Pattern.compile("\\)\\s*-\\s*(.*)$")
                .matcher(value);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "OUTROS";
    }

    public DepartmentReportDTO getPrintsByDepartment(String unit) throws Exception {

        try {

            List<PrintData> prints = readCsv();
            List<String> ips = getIpsByUnit(unit);

            List<PrintData> printsFiltered = prints.stream()
                    .filter(print -> ips.stream()
                            .anyMatch(ip -> print.getEnderecoDispositivo().startsWith(ip)))
                    .toList();
            

            Map<String, Long> departments = printsFiltered.stream()
                    .collect(Collectors.groupingBy(
                            print -> extractDepartment(print.getDepartamentoDispositivo()),
                            Collectors.summingLong(PrintData::getTotalImpresso)
                    ));

            List<PrintByDepartmentDTO> departmentList = departments.entrySet()
                    .stream()
                    .map(entry -> new PrintByDepartmentDTO(
                            entry.getKey(),
                            entry.getValue()
                    ))
                    .toList();

            return new DepartmentReportDTO(
                    unit,
                    departmentList
            );

        } catch (Exception e) {
            throw new Exception("Failed to filter prints by department: " + e.getMessage());
        }
    }




}
