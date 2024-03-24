package com.axonactive.dojo.employee.service;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.assignment.mapper.AssignmentMapper;
import com.axonactive.dojo.base.cache.BaseCache;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.base.message.LoggerMessage;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.employee.dto.*;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.employee.mapper.EmployeeMapper;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.enums.Gender;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dto.ProjectListResponseDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import netscape.javascript.JSObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Stateless
public class EmployeeService {

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private AssignmentMapper assignmentMapper;

    public EmployeeListResponseDTO findEmployees(long departmentId, int pageNumber, int pageSize, String name) throws EntityNotFoundException {
        List<Employee> employees;
        List<EmployeeDTO> employeeDTOS;
        long totalCount = 0L;

        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        if(departmentId > 0) {
            Department department = this.departmentDAO
                    .findActiveDepartmentById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

            if(name != null && !name.isEmpty()) {
                employees = this.employeeDAO.findEmployeesByNameAndDepartmentId(department.getId(), offset, pageSize, name);
                totalCount = this.employeeDAO.findTotalCountWithNameAndDepartmentId(department.getId(), name);
            }
            else {
                employees = this.employeeDAO.findEmployeesByDepartmentId(department.getId(), offset, pageSize);
                totalCount = this.employeeDAO.findTotalCountWithDepartmentId(department.getId());
            }

            employeeDTOS = this.employeeMapper.toListDTO(employees);

            return EmployeeListResponseDTO
                    .builder()
                    .employees(employeeDTOS)
                    .totalCount(totalCount)
                    .lastPage(((int)totalCount / pageSize) + 1)
                    .build();
        }

        if(name != null && !name.isEmpty()) {
            employees = this.employeeDAO.findEmployeesByName(offset, pageSize, name);

            employeeDTOS = this.employeeMapper.toListDTO(employees);
            totalCount = this.employeeDAO.findTotalCountWithName(name);
            return EmployeeListResponseDTO
                    .builder()
                    .employees(employeeDTOS)
                    .totalCount(totalCount)
                    .lastPage(((int)totalCount / pageSize) + 1)
                    .build();
        }

        employees = this.employeeDAO.findEmployees(offset, pageSize);
        employeeDTOS = this.employeeMapper.toListDTO(employees);
        totalCount = this.employeeDAO.findTotalCount();

        return EmployeeListResponseDTO
                .builder()
                .employees(employeeDTOS)
                .totalCount(totalCount)
                .lastPage(((int)totalCount / pageSize) + 1)
                .build();
    }

    public EmployeeDTO findActiveEmployeeById(long id) throws EntityNotFoundException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(id)
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        return this.employeeMapper.toDTO(employee);
    }

    public EmployeeDTO add(CreateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        Department department = this.departmentDAO
                .findActiveDepartmentById(reqDTO.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        Employee newEmployee = Employee
                .builder()
                .firstName(reqDTO.getFirstName())
                .middleName(reqDTO.getMiddleName())
                .lastName(reqDTO.getLastName())
                .dateOfBirth(reqDTO.getDateOfBirth())
                .gender(Gender.valueOf(reqDTO.getGender()))
                .salary(reqDTO.getSalary())
                .status(Status.ACTIVE)
                .department(department)
                .build();

        Employee employee = employeeDAO.add(newEmployee);

        return this.employeeMapper.toDTO(employee);
    }

    public EmployeeDTO update(UpdateEmployeeRequestDTO requestDTO) throws EntityNotFoundException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(requestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        Department department = this.departmentDAO
                .findActiveDepartmentById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setMiddleName(requestDTO.getMiddleName());
        employee.setGender(Gender.valueOf(requestDTO.getGender()));
        employee.setSalary(requestDTO.getSalary());
        employee.setDateOfBirth(requestDTO.getDateOfBirth());
        employee.setStatus(Status.ACTIVE);
        employee.setDepartment(department);

        Employee updatedEmployee = this.employeeDAO.update(employee);

        return this.employeeMapper.toDTO(updatedEmployee);
    }

    public DeleteSuccessMessage deleteSoftly(DeleteEmployeeRequestDTO requestDTO) throws EntityNotFoundException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(requestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        employee.setStatus(Status.DELETED);

        this.employeeDAO.update(employee);

        return EmployeeMessage.deleteSuccessMessage();
    }

    public EmployeeListResponseDTO findEmployeesByHoursInProjectMangedByDepartment(long departmentId, int pageNumber, int pageSize, int numberOfHour) throws EntityNotFoundException {
        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;
        List<Object[]> objects = new ArrayList<>();
        long totalCount = 0L;

        if(departmentId != 0) {
            Department department = departmentDAO.findActiveDepartmentById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));
            totalCount = employeeDAO.findTotalCountEmployeesByHoursInProjectMangedByDepartment(departmentId, numberOfHour);
            objects = employeeDAO.findEmployeesByHoursInProjectMangedByDepartment(department.getId(), offset, pageSize, numberOfHour);
        }
        else {
            totalCount = employeeDAO.findTotalCountEmployeesByHoursInProject(numberOfHour);
            objects = employeeDAO.findEmployeesByHoursInProject(offset, pageSize, numberOfHour);
        }

        Map<Long, EmployeeDTO> map = new HashMap<>();
        List<AssignmentDTO> assignmentDTOS = new ArrayList<>();
        objects.forEach(object -> {
            EmployeeDTO employeeDTO = employeeMapper.toDTO((Employee) object[0]);
            AssignmentDTO assignmentDTO = assignmentMapper.toDTO((Assignment) object[1]);
            map.put(employeeDTO.getId(),employeeDTO);
            assignmentDTOS.add(assignmentDTO);
        });

        return EmployeeListResponseDTO
                .builder()
                .employees(map.values().stream().peek(employeeDTO -> {
                    employeeDTO.setAssignments(assignmentDTOS.stream().filter((assignmentDTO -> Objects.equals(assignmentDTO.getEmployee().getId(), employeeDTO.getId()))).toList());
                }).toList())
                .totalCount(totalCount)
                .lastPage(((int)totalCount / pageSize) + 1)
                .build();
    }

    public ByteArrayOutputStream exportEmployeeProfilesByHoursInProjectMangedByDepartment(long departmentId, int numberOfHour, String employeeIdsParam) throws DocumentException, IOException {
        List<Long> employeeIds = new ArrayList<>();

        String[] arr = employeeIdsParam.split(",");

        for(int i = 1; i< arr.length; i++) {
            employeeIds.add(Long.valueOf(arr[i]));
        }

        List<Object[]> objects = new ArrayList<>();

        if(departmentId != 0) {
            objects = employeeDAO.findEmployeesByHoursInProjectMangedByDepartment(departmentId, numberOfHour, employeeIds, arr[0]);
        }
        else {
            objects = employeeDAO.findEmployeesByHoursInProject(numberOfHour, employeeIds, arr[0]);
        }

        Map<Long, EmployeeDTO> map = new HashMap<>();
        List<AssignmentDTO> assignmentDTOS = new ArrayList<>();
        objects.forEach(object -> {
            EmployeeDTO employeeDTO = employeeMapper.toDTO((Employee) object[0]);
            AssignmentDTO assignmentDTO = assignmentMapper.toDTO((Assignment) object[1]);
            map.put(employeeDTO.getId(),employeeDTO);
            assignmentDTOS.add(assignmentDTO);
        });

        List<EmployeeDTO> employeeDTOS = map.values().stream().peek(employeeDTO -> {
            employeeDTO.setAssignments(assignmentDTOS.stream().filter((assignmentDTO -> Objects.equals(assignmentDTO.getEmployee().getId(), employeeDTO.getId()))).toList());
        }).toList();


        Map<String, ByteArrayOutputStream> pdfFiles = new HashMap<>();

        for(EmployeeDTO employeeDTO : employeeDTOS) {
            ByteArrayOutputStream outputStream = exportEmployeeProfile(employeeDTO);
            pdfFiles.put(UUID.randomUUID() + "_" + "employee" + "_" + "profile.pdf", outputStream);
        }

        return createZipFile(pdfFiles);
    }

    private ByteArrayOutputStream exportEmployeeProfile(EmployeeDTO employeeDTO) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        Font fontTittle = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, BaseColor.BLACK);
        Font fontSubTittle = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, BaseColor.BLACK);
        Font fontTable = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, BaseColor.BLACK);

        document.open();

        // Tittle
        Chunk tittle = new Chunk(String.format("%s's profiles", employeeDTO.getFirstName()), fontTittle);
        Phrase phrase = new Phrase();
        phrase.add(tittle);

        Paragraph para = new Paragraph();
        para.add(phrase);
        para.setAlignment(Element.ALIGN_CENTER);

        // Sub-tittle1
        Chunk subTittleInfo = new Chunk("\n\n1. Information\n\n", fontSubTittle);
        Phrase phraseInfo = new Phrase();
        phraseInfo.add(subTittleInfo);

        Paragraph paraInfo = new Paragraph();
        paraInfo.add(phraseInfo);
        paraInfo.setAlignment(Element.ALIGN_LEFT);


        // table info
        PdfPTable tableInfo = new PdfPTable(2);

        tableInfo.addCell(new PdfPCell(new Phrase(String.format("First name: %s", employeeDTO.getFirstName()), fontTable)));
        tableInfo.addCell(new PdfPCell(new Phrase(String.format("Last name: %s", employeeDTO.getLastName()), fontTable)));
        tableInfo.addCell(new PdfPCell(new Phrase(String.format("Middle name: %s", employeeDTO.getMiddleName()), fontTable)));
        tableInfo.addCell(new PdfPCell(new Phrase(String.format("Date of birth: %s", employeeDTO.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), fontTable)));
        tableInfo.addCell(new PdfPCell(new Phrase(String.format("Salary: %f", employeeDTO.getSalary()), fontTable)));
        tableInfo.addCell(new PdfPCell(new Phrase(String.format("Department: %s", employeeDTO.getDepartment().getDepartmentName()), fontTable)));
        tableInfo.addCell(new PdfPCell(new Phrase(String.format("Gender: %s", employeeDTO.getGender().toString().toLowerCase()), fontTable)));
        tableInfo.addCell("");

        // Sub-tittle2
        Chunk subTittleAssignment = new Chunk("2. Assignments\n\n", fontSubTittle);
        Phrase phraseAssignment = new Phrase();
        phraseAssignment.add(subTittleAssignment);

        Paragraph paraAssignment = new Paragraph();
        paraAssignment.add(phraseAssignment);
        paraAssignment.setAlignment(Element.ALIGN_LEFT);

        // table assignments
        PdfPTable tableAssignment = new PdfPTable(5);

        Stream.of("No.", "Project's name", "Area", "Number of hours", "Project's department")
                .forEach(columnTittle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPhrase(new Phrase(columnTittle, fontTable));
                    tableAssignment.addCell(header);
                });

        List<AssignmentDTO> assignmentDTOS = employeeDTO.getAssignments();

        for(int i = 0; i < assignmentDTOS.size(); i++) {
            AssignmentDTO assignmentDTO = assignmentDTOS.get(i);
            tableAssignment.addCell(new PdfPCell(new Phrase(String.format("%d", i + 1), fontTable)));
            tableAssignment.addCell(new PdfPCell(new Phrase(assignmentDTO.getProject().getProjectName(), fontTable)));
            tableAssignment.addCell(new PdfPCell(new Phrase(assignmentDTO.getProject().getArea(), fontTable)));
            tableAssignment.addCell(new PdfPCell(new Phrase(String.format("%d", assignmentDTO.getNumberOfHour()), fontTable)));
            tableAssignment.addCell(new PdfPCell(new Phrase(assignmentDTO.getProject().getDepartment().getDepartmentName(), fontTable)));
        }

        document.add(para);
        document.add(paraInfo);
        document.add(tableInfo);
        document.add(paraAssignment);
        document.add(tableAssignment);
        document.close();

        return outputStream;
    }

    private void createZipEntry(ZipOutputStream zos, ByteArrayOutputStream pdfFile, String filename) throws IOException {
        ZipEntry entry = new ZipEntry(filename);
        entry.setSize(pdfFile.toByteArray().length);
        zos.putNextEntry(entry);
        zos.write(pdfFile.toByteArray());
    }

    private ByteArrayOutputStream createZipFile(Map<String, ByteArrayOutputStream> pdfFiles) throws IOException {
        ByteArrayOutputStream zipFile = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(zipFile);

        for(Map.Entry<String, ByteArrayOutputStream> entry : pdfFiles.entrySet()) {
            createZipEntry(zos, entry.getValue(), entry.getKey());
        }

        zos.closeEntry();
        zos.close();

        return zipFile;
    }
}
