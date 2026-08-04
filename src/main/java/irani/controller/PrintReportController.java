package irani.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import irani.DTO.DepartmentReportDTO;
import irani.DTO.PrintByDepartmentDTO;
import irani.DTO.PrintsByUnitDTO;
import irani.entities.PrintData;
import irani.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/prints")
public class PrintReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<List<PrintData>> getReports () throws Exception {
        return ResponseEntity.ok(reportService.readCsv());
    }

    @GetMapping("/report/unit")
    public ResponseEntity<List<PrintsByUnitDTO>> getReportUnits (@RequestParam(required = false) String unit) throws Exception {
        return ResponseEntity.ok(reportService.getPrintsByUnit(unit));
    }

    @GetMapping("/report/unit/department")
    public ResponseEntity<DepartmentReportDTO> getReportUnitDepartments (@RequestParam String unit) throws Exception {
        return ResponseEntity.ok(reportService.getPrintsByDepartment(unit));
    }
}
