package com.axonactive.dojo.project.service;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dao.ProjectDAO;
import com.axonactive.dojo.project.dto.*;
import com.axonactive.dojo.project.entity.Project;
import com.axonactive.dojo.project.mapper.ProjectMapper;
import com.axonactive.dojo.project.message.ProjectMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProjectService {

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private ProjectMapper projectMapper;

    public List<ProjectDTO> findAll() {
        List<Project> projects = this.projectDAO.findAll();
        return this.projectMapper.toListDTO(projects);
    }

    public ProjectListResponseDTO findProjects(long departmentId, int pageNumber, int pageSize) throws EntityNotFoundException {
        List<Project> projects;
        List<ProjectDTO> projectDTOS;
        long totalCount = 0L;

        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        if(departmentId > 0) {
            Department department = this.departmentDAO
                    .findActiveDepartmentById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

            projects = this.projectDAO.findProjectsByDepartmentId(department.getId(), offset, pageSize);
            projectDTOS = this.projectMapper.toListDTO(projects);
            totalCount = this.projectDAO.findTotalCountWithDepartmentId(department.getId());
            return ProjectListResponseDTO
                    .builder()
                    .projects(projectDTOS)
                    .totalCount(totalCount)
                    .lastPage(((int)totalCount / pageSize) + 1)
                    .build();
        }

        projects = this.projectDAO.findProjects(offset, pageSize);
        projectDTOS = this.projectMapper.toListDTO(projects);
        totalCount = this.projectDAO.findTotalCount();

        return ProjectListResponseDTO
                .builder()
                .projects(projectDTOS)
                .totalCount(totalCount)
                .lastPage(((int)totalCount / pageSize) + 1)
                .build();
    }

    public ProjectDTO findById(long id) throws EntityNotFoundException {
        Project project = this.projectDAO.findActiveProjectById(id).orElseThrow(() -> new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT));

        return this.projectMapper.toDTO(project);
    }


    public ProjectsWithEmployeesListDTO findProjectsWithEmployeesSalariesHours(int pageNumber, int pageSize, long numberOfEmployees,
                                                                               long totalHours, BigDecimal totalSalaries) {
        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;
        List<ProjectsWithEmployeesDTO> list = projectDAO.findProjectsWithEmployeesSalariesHours(offset, pageSize, numberOfEmployees, totalHours, totalSalaries);
        ProjectCountDTO projectCountDTO = projectDAO.findTotalCountProjectsWithEmployeesSalariesHours(numberOfEmployees, totalHours, totalSalaries);

        return ProjectsWithEmployeesListDTO
                .builder()
                .projects(list)
                .totalCount(projectCountDTO.getTotalCount())
                .lastPage(((int)projectCountDTO.getTotalCount() / pageSize) + 1)
                .build();
    }

    public ByteArrayOutputStream exportExcelProjectsWithEmployeesSalariesHours(long numberOfEmployees, long totalHours, BigDecimal totalSalaries) throws Exception {
        try {
            List<ProjectsWithEmployeesDTO> list = projectDAO.findProjectsWithEmployeesSalariesHours(0, 1000, numberOfEmployees, totalHours, totalSalaries);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Books");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Price");

            for(int i = 1; i <= 10; i++ ) {
                Row row1 = sheet.createRow(i);
                row1.createCell(0).setCellValue("ID " + i);
                row1.createCell(1).setCellValue("Name " + i);
                row1.createCell(2).setCellValue("Price " + i);
            }

            // Write the Excel data to a ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream;
        }
        catch (Exception e) {
            throw e;
        }


    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
}
