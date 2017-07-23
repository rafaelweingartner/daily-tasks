package br.com.supero.desafio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.supero.desafio.beans.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query(value = "SELECT `order` FROM tasks WHERE username = ?1 and status <> 'Done' and status <> 'Removed' order by `order` desc limit 1", nativeQuery = true)
    Long getLargestOrderForUser(String username);

    @Query(value = "SELECT * FROM tasks WHERE username = ?1 and status <> 'Removed' order by `order` asc", nativeQuery = true)
    List<Task> findByUsernameNotRemovedTasksInOrder(String username);

}