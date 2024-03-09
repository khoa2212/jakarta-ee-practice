package com.axonactive.dojo.employee.service;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.assignment.mapper.AssignmentMapper;
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
import netscape.javascript.JSObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.*;
import java.util.function.Supplier;

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
        Department department = departmentDAO.findActiveDepartmentById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        long totalCount = employeeDAO.findTotalCountEmployeesByHoursInProjectMangedByDepartment(departmentId, numberOfHour);

        List<Object[]> objects = employeeDAO.findEmployeesByHoursInProjectMangedByDepartment(department.getId(), offset, pageSize, numberOfHour);

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
}
