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
import java.util.ArrayList;
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

    public ByteArrayOutputStream exportExcelProjectsWithEmployeesSalariesHours(long numberOfEmployees, long totalHours, BigDecimal totalSalaries, String projectIdsParam) throws Exception {
        List<Long> projectIds = new ArrayList<>();

        String[] arr = projectIdsParam.split(",");

        for(int i = 1; i< arr.length; i++) {
            projectIds.add(Long.valueOf(arr[i]));
        }

        System.out.println(projectIds.get(0));
        List<ProjectsWithEmployeesDTO> list = projectDAO.findProjectsWithEmployeesSalariesHours(numberOfEmployees, totalHours, totalSalaries, projectIds, arr[0]);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Project with salaries");
        setWidthColumn(sheet);

        List<String> titles = new ArrayList<>(List.of("No.", "Project's name", "Area", "Number of employees", "Total hours", "Total salaries"));

        createHeader(sheet, titles);
        createReport(sheet, list, titles);

        sheet.createFreezePane(0, 1);

        // Write the Excel data to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return outputStream;


    }

    private CellStyle createCellStyle(Sheet sheet, HorizontalAlignment alignment, int cellColor, int fontColor, boolean bold) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Calibri");
        font.setBold(bold);
        font.setFontHeightInPoints((short) 11);
        font.setColor((short) fontColor);

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor((short) cellColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setAlignment(alignment);

        return cellStyle;
    }

    private void setWidthColumn(Sheet sheet) {
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 30 * 256);
        sheet.setColumnWidth(5, 30 * 256);
    }

    private void createHeader(Sheet sheet, List<String> titles) {
        Row row = sheet.createRow(0);

        for(int i = 0; i < titles.size(); i++) {
            row.createCell(i).setCellValue(titles.get(i));
            row.getCell(i).setCellStyle(createCellStyle(sheet, HorizontalAlignment.CENTER, IndexedColors.LIGHT_BLUE.getIndex(), IndexedColors.WHITE.getIndex(), true));
        }
    }

    private void createReport(Sheet sheet, List<ProjectsWithEmployeesDTO> list, List<String> titles) {
        for(int i = 0; i < list.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            row1.createCell(0).setCellValue(i + 1);
            row1.createCell(1).setCellValue(list.get(i).getProjectName());
            row1.createCell(2).setCellValue(list.get(i).getArea());
            row1.createCell(3).setCellValue(list.get(i).getNumberOfEmployees());
            row1.createCell(4).setCellValue(list.get(i).getTotalHours());
            row1.createCell(5).setCellValue(list.get(i).getTotalSalaries().doubleValue());

            if(i % 2 == 0) {
                for(int j = 0; j < titles.size(); j++) {
                    HorizontalAlignment alignment = HorizontalAlignment.LEFT;
                    if(j >= 3) {
                        alignment = HorizontalAlignment.RIGHT;
                    }

                    row1.getCell(j).setCellStyle(createCellStyle(sheet, alignment, IndexedColors.LIGHT_TURQUOISE1.getIndex(), IndexedColors.BLACK.getIndex(), false));
                }
            }
            else {
                for(int j = 0; j < titles.size(); j++) {
                    HorizontalAlignment alignment = HorizontalAlignment.LEFT;
                    if(j >= 3) {
                        alignment = HorizontalAlignment.RIGHT;
                    }

                    row1.getCell(j).setCellStyle(createCellStyle(sheet, alignment, IndexedColors.WHITE.getIndex(), IndexedColors.BLACK.getIndex(), false));
                }
            }
        }
    }
}
