package com.example.payroll

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EmployeeController(private val repository: EmployeeRepository) {
    @GetMapping("/employees")
    fun all() = repository.findAll()

    @PostMapping("/employees")
    fun newEmployee(@RequestBody newEmployee: Employee) =
        repository.save(newEmployee)

    @GetMapping("/employees/{id}")
    fun one(@PathVariable id: Long) =
        repository.findById(id).orElseThrow { EmployeeNotFoundException(id) }

    @PutMapping("/employees/{id}")
    fun replaceEmployee(@RequestBody newEmployee: Employee, @PathVariable id: Long) =
        repository.findById(id)
            .map {
                it.name = newEmployee.name
                it.role = newEmployee.role
                repository.save(it)
            }
            .orElseGet {
                newEmployee.id = id
                repository.save(newEmployee)
            }

    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(@PathVariable id: Long) =
        repository.deleteById(id)
}
