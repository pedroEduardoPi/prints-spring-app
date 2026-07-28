package irani.DTO;
import java.util.List;

public record DepartmentReportDTO(
        String unit,
        List<PrintByDepartmentDTO> departments
) {}
