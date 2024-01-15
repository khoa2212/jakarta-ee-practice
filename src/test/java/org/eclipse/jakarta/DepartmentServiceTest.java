package org.eclipse.jakarta;

import org.eclipse.jakarta.department.entity.Department;
import org.eclipse.jakarta.department.repository.DepartmentRepository;
import org.eclipse.jakarta.department.service.DepartmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @InjectMocks
    DepartmentService departmentService;

    @Mock
    DepartmentRepository departmentRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void createDepartment() {
        var department = Department.builder().departmentName("test department").startDate(LocalDate.now()).build();
        var testDepartment = departmentService.createDepartment(department);
        assertEquals(true, true);
    }

    @Test
    public void getDepartments() {
        List<Department> list = departmentService.getDepartments();
        System.out.println("ljds");
        assertEquals(0, list.size());
    }
}
