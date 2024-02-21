package com.axonactive.dojo.employee.service;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
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
import netscape.javascript.JSObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class EmployeeService {

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private EmployeeMapper employeeMapper;

    public EmployeeListResponseDTO findEmployees(Long departmentId, Integer pageNumber, Integer pageSize, String name) throws EntityNotFoundException {
        List<Employee> employees;
        List<EmployeeDTO> employeeDTOS;
        Long totalCount = 0L;

        if(departmentId > 0) {
            Optional<Department> department = this.departmentDAO.findById(departmentId);

            if(department.isEmpty() || department.get().getStatus() == Status.DELETED) {
                throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
            }

            if(name != null && name.length() != 0) {
                employees = this.employeeDAO.findEmployeesByNameAndDepartmentId(departmentId, pageNumber, pageSize, name);
                totalCount = this.employeeDAO.findTotalCountWithNameAndDepartmentId(departmentId, name);
            }
            else {
                employees = this.employeeDAO.findEmployeesByDepartmentId(departmentId, pageNumber, pageSize);
                totalCount = this.employeeDAO.findTotalCountWithDepartmentId(departmentId);
            }

            employeeDTOS = this.employeeMapper.toListDTO(employees);

            return EmployeeListResponseDTO
                    .builder()
                    .employees(employeeDTOS)
                    .totalCount(totalCount)
                    .lastPage((totalCount.intValue() / pageSize) + 1)
                    .build();
        }

        if(name != null && name.length() != 0) {
            employees = this.employeeDAO.findEmployeesByName(pageNumber, pageSize, name);

            employeeDTOS = this.employeeMapper.toListDTO(employees);
            totalCount = this.employeeDAO.findTotalCountWithName(name);
            return EmployeeListResponseDTO
                    .builder()
                    .employees(employeeDTOS)
                    .totalCount(totalCount)
                    .lastPage((totalCount.intValue() / pageSize) + 1)
                    .build();
        }

        employees = this.employeeDAO.findEmployees(pageNumber, pageSize);
        employeeDTOS = this.employeeMapper.toListDTO(employees);
        totalCount = this.employeeDAO.findTotalCount();

        return EmployeeListResponseDTO
                .builder()
                .employees(employeeDTOS)
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();
    }

    public EmployeeDTO findById(Long id) throws EntityNotFoundException {
        Optional<Employee> optionalEmployee = this.employeeDAO.findById(id);

        if(optionalEmployee.isEmpty() || optionalEmployee.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE);
        }

        return this.employeeMapper.toDTO(optionalEmployee.get());
    }

    public EmployeeDTO add(CreateEmployeeRequestDTO reqDTO) throws EntityNotFoundException {
        Optional<Department> department = this.departmentDAO.findById(reqDTO.getDepartmentId());

        if(department.isEmpty() || department.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
        }

        Employee newEmployee = Employee
                .builder()
                .firstName(reqDTO.getFirstName())
                .middleName(reqDTO.getMiddleName())
                .lastName(reqDTO.getLastName())
                .dateOfBirth(reqDTO.getDateOfBirth())
                .gender(Gender.valueOf(reqDTO.getGender()))
                .salary(reqDTO.getSalary())
                .status(Status.ACTIVE)
                .department(department.get())
                .build();

        Employee employee = employeeDAO.add(newEmployee);

        return this.employeeMapper.toDTO(employee);
    }

    public EmployeeDTO update(UpdateEmployeeRequestDTO requestDTO) throws EntityNotFoundException {
        Optional<Employee> optionalEmployee = this.employeeDAO.findById(requestDTO.getId());

        if(optionalEmployee.isEmpty() || optionalEmployee.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE);
        }

        Optional<Department> department = this.departmentDAO.findById(requestDTO.getDepartmentId());

        if(department.isEmpty() || department.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
        }

        Employee employee = optionalEmployee.get();

        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setMiddleName(requestDTO.getMiddleName());
        employee.setGender(Gender.valueOf(requestDTO.getGender()));
        employee.setSalary(requestDTO.getSalary());
        employee.setDateOfBirth(requestDTO.getDateOfBirth());
        employee.setStatus(Status.ACTIVE);
        employee.setDepartment(department.get());

        Employee updatedEmployee = this.employeeDAO.update(employee);

        return this.employeeMapper.toDTO(updatedEmployee);
    }

    public DeleteSuccessMessage deleteSoftly(DeleteEmployeeRequestDTO requestDTO) throws EntityNotFoundException {
        Optional<Employee> optionalEmployee = this.employeeDAO.findById(requestDTO.getId());

        if(optionalEmployee.isEmpty() || optionalEmployee.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE);
        }

        Employee employee = optionalEmployee.get();
        employee.setStatus(Status.DELETED);

        this.employeeDAO.update(employee);

        return EmployeeMessage.deleteSuccessMessage();
    }
}
