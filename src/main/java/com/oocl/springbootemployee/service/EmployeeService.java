package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAll(Gender gender) {
        return employeeRepository.findEmployeesByGender(gender);
    }

    public Page<Employee> findAll(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return employeeRepository.findAll(pageable);
    }

    public Employee findById(Integer employeeId) {
        return employeeRepository.findEmployeeById(employeeId);
    }

    public Employee create(Employee employee) {
        if (employee.getAge() < 18 || employee.getAge() > 65)
            throw new EmployeeAgeNotValidException();
        if (employee.getAge() >= 30 && employee.getSalary() < 20000.0)
            throw new EmployeeAgeSalaryNotMatchedException();

        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    public Employee update(Integer employeeId, Employee employee) {
        Employee employeeExisted = employeeRepository.findEmployeeById(employeeId);
        if (Boolean.FALSE.equals(employeeExisted.getActive()))
            throw new EmployeeInactiveException();
        return employeeRepository.save(employee);
    }

    public void delete(Integer employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
