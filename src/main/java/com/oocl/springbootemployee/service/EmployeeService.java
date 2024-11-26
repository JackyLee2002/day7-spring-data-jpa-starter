package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeInMemoryRepository;

import java.util.List;

import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class EmployeeService {
    private final EmployeeInMemoryRepository employeeInMemoryRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeInMemoryRepository employeeInMemoryRepository, EmployeeRepository employeeRepository) {
        this.employeeInMemoryRepository = employeeInMemoryRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAll(Gender gender) {
        return employeeRepository.findEmployeesByGender(gender);
    }

    public Page<Employee> findAll(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page-1, pageSize);
        return employeeRepository.findAll(pageable);
    }

    public Employee findById(Integer employeeId) {
        return employeeRepository.findEmployeeById(employeeId);
    }

    public Employee create(Employee employee) {
        if(employee.getAge() < 18 || employee.getAge() > 65)
            throw new EmployeeAgeNotValidException();
        if(employee.getAge() >= 30 && employee.getSalary() < 20000.0)
            throw new EmployeeAgeSalaryNotMatchedException();

        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    private Employee updateEmployeeAttributes(Employee employeeStored, Employee newEmployee) {
        if (newEmployee.getName() != null) {
            employeeStored.setName(newEmployee.getName());
        }
        if (newEmployee.getAge() != null) {
            employeeStored.setAge(newEmployee.getAge());
        }
        if (newEmployee.getGender() != null) {
            employeeStored.setGender(newEmployee.getGender());
        }
        if (newEmployee.getSalary() != null) {
            employeeStored.setSalary(newEmployee.getSalary());
        }
        return employeeStored;
    }

    public Employee update(Integer employeeId , Employee employee) {
        Employee employeeExisted = employeeRepository.findEmployeeById(employeeId);
        if(Boolean.FALSE.equals(employeeExisted.getActive()))
            throw new EmployeeInactiveException();

        Employee updatedEmployee = employeeRepository.findAll().stream()
                .filter(storedEmployee -> storedEmployee.getId().equals(employeeId))
                .findFirst()
                .map(storedEmployee -> updateEmployeeAttributes(storedEmployee, employee))
                .orElse(null);

        if (updatedEmployee == null) {
            return null;
        }


        return employeeRepository.save(updatedEmployee);
    }

    public void delete(Integer employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
