package br.com.supero.desafio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.supero.desafio.beans.Task;
import br.com.supero.desafio.services.TaskService;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ResponseBody
    @RequestMapping(value = "/task", method = {RequestMethod.POST, RequestMethod.PUT})
    public Object addTask(@RequestBody Task task) {
        taskService.saveOrUpdate(task);
        return task;
    }

    @ResponseBody
    @RequestMapping(value = "/tasks/{username}", method = RequestMethod.GET)
    public Object listTasks(@PathVariable String username) {
        return taskService.findAllTasksUser(username);
    }

}