package com.oocl.springbootemployee.repository;

import com.oocl.springbootemployee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExmployeeRepository extends JpaRepository<Employee, Integer> {

}
