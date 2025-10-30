package io.github.khayrulinnikita

import io.github.khayrulinnikita.store.ActionStore

import java.util.concurrent.*

import io.github.khayrulinnikita.model.Action
import io.github.khayrulinnikita.model.Task
import io.github.khayrulinnikita.service.EventBus
import io.github.khayrulinnikita.service.ToDoListManager
import io.github.khayrulinnikita.service.TrackActionService
import io.github.khayrulinnikita.store.TaskStore
import io.github.khayrulinnikita.view.ConsoleView

import java.time.LocalDateTime

class Main {
    static void main(String[] args) {
        Task test = new Task("test task", LocalDateTime.now(), LocalDateTime.now().plusHours(2))
        Action action = new Action("test action", LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2))
        TaskStore taskStore = new TaskStore()
        ActionStore actionStore = new ActionStore()
        EventBus eventBus = new EventBus()
        ToDoListManager manager = new ToDoListManager(taskStore, actionStore)
        ConsoleView consoleView = new ConsoleView(manager, eventBus)
        TrackActionService trackActionService = new TrackActionService(eventBus, actionStore)
        manager.addTask(test)
        manager.addAction(test.id as String, action)
        def executor = Executors.newCachedThreadPool()

        executor.submit {
            trackActionService.onSchedule()
        }
        executor.submit {
            consoleView.dialogMenu()
        }
    }
}

