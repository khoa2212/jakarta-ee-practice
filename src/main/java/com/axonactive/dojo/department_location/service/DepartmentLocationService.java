package com.axonactive.dojo.department_location.service;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.mapper.DepartmentMapper;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.department.service.DepartmentService;
import com.axonactive.dojo.department_location.dao.DepartmentLocationDAO;
import com.axonactive.dojo.department_location.dto.*;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.department_location.mapper.DepartmentLocationMapper;
import com.axonactive.dojo.department_location.message.DepartmentLocationMessage;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentLocationService {

        @Inject
        private DepartmentLocationDAO departmentLocationDAO;

        @Inject
        private DepartmentDAO departmentDAO;

        @Inject
        private DepartmentLocationMapper departmentLocationMapper;

        @Inject
        private DepartmentMapper departmentMapper;

        public DepartmentLocationListResponseDTO findDepartmentsLocationByDepartmentId(long departmentId,
                        int pageNumber, int pageSize) throws EntityNotFoundException {
                Department department = this.departmentDAO
                                .findActiveDepartmentById(departmentId)
                                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

                int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

                List<DepartmentLocation> departmentLocations = this.departmentLocationDAO
                                .findDepartmentsLocationByDepartmentId(department.getId(), offset, pageSize);

                long totalCount = this.departmentLocationDAO.findTotalCount(department.getId());

                return DepartmentLocationListResponseDTO
                                .builder()
                                .departmentLocations(this.departmentLocationMapper.toListDTO(departmentLocations))
                                .totalCount(totalCount)
                                .lastPage(((int) totalCount / pageSize) + 1)
                                .build();
        }

        public DepartmentLocationDTO add(CreateDepartmentLocationRequestDTO createDepartmentLocationRequestDTO)
                        throws EntityNotFoundException, BadRequestException {
                Department department = this.departmentDAO
                                .findActiveDepartmentById(createDepartmentLocationRequestDTO.getDepartmentId())
                                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

                Optional<DepartmentLocation> optionalDepartmentLocation = this.departmentLocationDAO
                                .findDepartmentLocationByDepartmentId(
                                                createDepartmentLocationRequestDTO.getLocation().trim().toLowerCase(),
                                                createDepartmentLocationRequestDTO.getDepartmentId());

                if (optionalDepartmentLocation.isPresent()) {
                        throw new BadRequestException(DepartmentLocationMessage.EXISTED_LOCATION);
                }

                DepartmentLocation newDepartmentLocation = DepartmentLocation
                                .builder()
                                .location(createDepartmentLocationRequestDTO.getLocation())
                                .department(department)
                                .build();

                DepartmentLocation departmentLocation = this.departmentLocationDAO.add(newDepartmentLocation);

                return this.departmentLocationMapper.toDTO(departmentLocation);
        }

        public DepartmentLocationDTO update(UpdateDepartmentLocationRequestDTO updateDepartmentLocationRequestDTO)
                        throws EntityNotFoundException, BadRequestException {
                Department department = this.departmentDAO
                                .findActiveDepartmentById(updateDepartmentLocationRequestDTO.getDepartmentId())
                                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

                DepartmentLocation departmentLocation = this.departmentLocationDAO
                                .findById(updateDepartmentLocationRequestDTO.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                DepartmentLocationMessage.NOT_FOUND_LOCATION));

                Optional<DepartmentLocation> optionalDepartmentLocation = this.departmentLocationDAO
                                .findDepartmentLocationByDepartmentId(
                                                updateDepartmentLocationRequestDTO.getLocation().trim().toLowerCase(),
                                                updateDepartmentLocationRequestDTO.getDepartmentId());

                if (optionalDepartmentLocation.isPresent()
                                && optionalDepartmentLocation.get().getId() != departmentLocation.getId()) {
                        throw new BadRequestException(DepartmentLocationMessage.EXISTED_LOCATION);
                }

                departmentLocation.setLocation(updateDepartmentLocationRequestDTO.getLocation());
                departmentLocation.setDepartment(department);

                DepartmentLocation updatedDepartmentLocation = this.departmentLocationDAO.update(departmentLocation);
                return this.departmentLocationMapper.toDTO(updatedDepartmentLocation);
        }

        public DeleteSuccessMessage delete(DeleteDepartmentLocationRequestDTO deleteDepartmentLocationRequestDTO)
                        throws EntityNotFoundException {
                DepartmentLocation departmentLocation = this.departmentLocationDAO
                                .findById(deleteDepartmentLocationRequestDTO.getId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                DepartmentLocationMessage.NOT_FOUND_LOCATION));

                this.departmentLocationDAO.delete(departmentLocation.getId());

                return DepartmentLocationMessage.deleteSuccessMessage();
        }
}
