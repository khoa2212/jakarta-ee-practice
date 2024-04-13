package com.axonactive.dojo.employee.service;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.assignment.mapper.AssignmentMapper;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.base.producer.RabbitMQProducer;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.dto.*;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.mapper.EmployeeMapper;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.enums.Gender;
import com.axonactive.dojo.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rabbitmq.client.BuiltinExchangeType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeoutException;
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

    @Inject
    private RabbitMQProducer rabbitMQProducer;

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

    public EmployeeDTO findActiveEmployeeById(long id) throws EntityNotFoundException, IOException, TimeoutException {
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
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        HeaderFooterPageEvent headerFooterPageEvent = new HeaderFooterPageEvent();

        Font fontTittle = FontFactory.getFont(FontFactory.HELVETICA, 25, BaseColor.BLACK);
        fontTittle.setStyle(Font.BOLD);
        Font fontSubTittle = FontFactory.getFont(FontFactory.HELVETICA, 15, BaseColor.RED);
        fontSubTittle.setStyle(Font.ITALIC);
        Font fontTable = FontFactory.getFont(FontFactory.HELVETICA, 13, BaseColor.BLACK);

        document.open();

        // make header and footer
        writer.setPageEvent(headerFooterPageEvent);

        // Tittle
        Chunk tittle = new Chunk(String.format("%s's profiles", employeeDTO.getFirstName()), fontTittle);
        Phrase phrase = new Phrase();
        phrase.add(tittle);

        Paragraph para = new Paragraph();
        para.add(phrase);
        para.setAlignment(Element.ALIGN_CENTER);
        para.setSpacingBefore(20);
        para.setSpacingAfter(15);

        // Sub-tittle1
        Chunk subTittleInfo = new Chunk("1. Information", fontSubTittle);
        Phrase phraseInfo = new Phrase();
        phraseInfo.add(subTittleInfo);

        Paragraph paraInfo = new Paragraph();
        paraInfo.add(phraseInfo);
        paraInfo.setAlignment(Element.ALIGN_LEFT);

        // table info
        PdfPTable tableInfo = new PdfPTable(2);

        tableInfo.setSpacingBefore(15);
        tableInfo.setSpacingAfter(10);

        tableInfo.setWidthPercentage(100);

        PdfPCell pdfPCellFirstName = new PdfPCell(new Phrase(String.format("First name: %s", employeeDTO.getFirstName()), fontTable));
        pdfPCellFirstName.setPaddingBottom(10);
        pdfPCellFirstName.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPCellLastName = new PdfPCell(new Phrase(String.format("Last name: %s", employeeDTO.getLastName()), fontTable));
        pdfPCellLastName.setPaddingBottom(10);
        pdfPCellLastName.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPCellMiddleName = new PdfPCell(new Phrase(String.format("Middle name: %s", employeeDTO.getMiddleName() == null ? "" : employeeDTO.getMiddleName()), fontTable));
        pdfPCellMiddleName.setPaddingBottom(10);
        pdfPCellMiddleName.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPCellDateOfBirth = new PdfPCell(new Phrase(String.format("Date of birth: %s", employeeDTO.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))), fontTable));
        pdfPCellDateOfBirth.setPaddingBottom(10);
        pdfPCellDateOfBirth.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPCellSalary = new PdfPCell(new Phrase(String.format("Salary: %f", employeeDTO.getSalary()), fontTable));
        pdfPCellSalary.setPaddingBottom(10);
        pdfPCellSalary.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPCellDepartment = new PdfPCell(new Phrase(String.format("Department: %s", employeeDTO.getDepartment().getDepartmentName()), fontTable));
        pdfPCellDepartment.setPaddingBottom(10);
        pdfPCellDepartment.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPCellGender = new PdfPCell(new Phrase(String.format("Gender: %s", employeeDTO.getGender().toString().toLowerCase()), fontTable));
        pdfPCellGender.setPaddingBottom(10);
        pdfPCellGender.setBorder(Rectangle.NO_BORDER);


        PdfPCell pdfPCellNone = new PdfPCell();
        pdfPCellNone.setPaddingBottom(10);
        pdfPCellNone.setBorder(Rectangle.NO_BORDER);

        tableInfo.addCell(pdfPCellFirstName);
        tableInfo.addCell(pdfPCellLastName);
        tableInfo.addCell(pdfPCellMiddleName);
        tableInfo.addCell(pdfPCellDateOfBirth);
        tableInfo.addCell(pdfPCellSalary);
        tableInfo.addCell(pdfPCellDepartment);
        tableInfo.addCell(pdfPCellGender);
        tableInfo.addCell(pdfPCellNone);

        // Sub-tittle2
        Chunk subTittleAssignment = new Chunk("2. Assignments", fontSubTittle);
        Phrase phraseAssignment = new Phrase();
        phraseAssignment.add(subTittleAssignment);

        Paragraph paraAssignment = new Paragraph();
        paraAssignment.add(phraseAssignment);
        paraAssignment.setAlignment(Element.ALIGN_LEFT);

        // table assignments
        PdfPTable tableAssignment = new PdfPTable(5);

        tableAssignment.setSpacingBefore(15);
        tableAssignment.setSpacingAfter(10);

        tableAssignment.setWidthPercentage(100);
        tableAssignment.setWidths(new int[] {10,25,15,25,25});
        tableAssignment.setHeaderRows(0);

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

        document.add(new Chunk(""));
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
