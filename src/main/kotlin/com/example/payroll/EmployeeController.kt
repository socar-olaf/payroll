package com.example.payroll

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
class EmployeeController(private val repository: EmployeeRepository) {
    @GetMapping("/employees")
    fun all(): CollectionModel<EntityModel<Employee>> {
        val employees: List<EntityModel<Employee>> = repository.findAll().stream().map { employee ->
            EntityModel(
                employee,
                linkTo(methodOn(EmployeeController::class.java).one(employee.id!!)).withSelfRel(),
                linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees")
            )
        }.collect(Collectors.toList())

        return CollectionModel(
            employees,
            linkTo(methodOn(EmployeeController::class.java).all()).withSelfRel()
        )
    }

    @PostMapping("/employees")
    fun newEmployee(@RequestBody newEmployee: Employee) =
        repository.save(newEmployee)

    @GetMapping("/employees/{id}")
    fun one(@PathVariable id: Long): EntityModel<Employee> {
        val employee = repository.findById(id).orElseThrow { EmployeeNotFoundException(id) }
        return EntityModel(
            employee,
            linkTo(methodOn(EmployeeController::class.java).one(id)).withSelfRel(),
            linkTo(methodOn(EmployeeController::class.java).all()).withRel("employees")
        )
    }

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
