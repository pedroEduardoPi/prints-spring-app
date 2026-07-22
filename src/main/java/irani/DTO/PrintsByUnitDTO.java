package irani.DTO;
import java.util.List;

public record PrintsByUnitDTO(String unit, List<PrintsByDateDTO> prints) {}
