package br.com.supero.desafio.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.supero.desafio.beans.Task;
import br.com.supero.desafio.exceptions.TaskException;
import br.com.supero.desafio.repositories.TaskRepository;

@Service
@Transactional(readOnly = true)
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Transactional(readOnly = false)
    public Task saveOrUpdate(Task task) {
        validateTaskBeforeExecutingOperation(task);
        if (task.getOrder() == null) {
            Long largestOrderForUser = getNextOrderNumberForUser(task);
            task.setOrder(largestOrderForUser);
        }
        taskRepository.save(task);
        return task;
    }

    private Long getNextOrderNumberForUser(Task task) {
        Long largestOrderForUser = taskRepository.getLargestOrderForUser(task.getUsername());
        if (largestOrderForUser == null) {
            return 0l;
        }
        return largestOrderForUser + 1;
    }

    private void validateTaskBeforeExecutingOperation(Task task) {
        String username = task.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new TaskException("Username cannot be null or empty. Did you login?");
        }
        if (task.getId() != null) {
            Task taskInDatabase = taskRepository.findOne(task.getId());
            if (taskInDatabase == null) {
                throw new TaskException("The task you sent for update does not exist in the database.");
            }
            if (!taskInDatabase.getUsername().equals(username)) {
                throw new TaskException("Why are you trying to change some other user's task?");
            }
        }
    }

    public Object deleteTask(Task task) {
        validateTaskBeforeExecutingOperation(task);
        taskRepository.delete(task);
        return task;
    }

    public void delete(Task task) {
        validateTaskBeforeExecutingOperation(task);
        taskRepository.delete(task);
    }

    public List<Task> findAllTasksUser(String username) {
        return taskRepository.findByUsernameNotRemovedTasksInOrder(username);
    }
}
