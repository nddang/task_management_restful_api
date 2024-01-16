package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    
    //return the list of tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    //return the optional task
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    //created task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    //update task
    public Task updateTask(Long id, Task task) {
        return taskRepository.save(task);
    }

    //delete task by id
    public void deleteTaskById(Long taskId) throws Exception {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            taskRepository.delete(task);
        } else {
        	throw new Exception("Task not found with ID: " + taskId);
        }
    }

    //delete a task from the repository.
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }
    
    //return the list of tasks with the user
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    //save a task in the repository.
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
}