package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskService taskService;
    private UserService userService;

    //get all tasks
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        // Call the TaskService to get all tasks
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    //get a task by id
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        // Call the TaskService to get the task by its ID
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //get all tasks for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long userId) {
        // Call the TaskService to get tasks by user ID
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    //create a new task
    @PostMapping("/")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task, @RequestParam("user_id") Long userId) {
        // Call the TaskService to create a new task
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    //update a task for a user
    @PutMapping("/user/{userId}/{taskId}")
    public ResponseEntity<Task> updateTaskForUser(@PathVariable Long userId, @PathVariable Long taskId,
                                                  @RequestBody Task updatedTask) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            // Get the tasks for the user
            List<Task> tasks = taskService.getTasksByUserId(userId);

            Optional<Task> optionalTask = tasks.stream().filter(task -> {
                System.out.println("Task ID: " + task.getId());
                System.out.println("Target Task ID: " + taskId);
                return task.getId().equals(taskId);
            }).findFirst();

            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                // Update the task properties with the provided values
                task.setName(updatedTask.getName());
                task.setStatus(updatedTask.getStatus());
                task.setComment(updatedTask.getComment());
                task.setTime(updatedTask.getTime());
                // Save the updated task
                Task savedTask = taskService.saveTask(task);
                return ResponseEntity.ok(savedTask);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a task by its ID
    @DeleteMapping("/user/{userId}/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        Optional<Task> optionalTask = taskService.getTaskById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            taskService.deleteTask(task);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}
