package io.github.khayrulinnikita.store

import io.github.khayrulinnikita.model.Task

class TaskStore {
    List<Task> tasks = []

    def add(Task task) {
        tasks.add(task)
    }

    def delete(Task task) {
        tasks.remove(task)
    }
}
