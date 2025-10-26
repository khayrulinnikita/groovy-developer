package io.github.khayrulinnikita.service

import io.github.khayrulinnikita.model.Action
import io.github.khayrulinnikita.model.Task
import io.github.khayrulinnikita.store.ActionStore
import io.github.khayrulinnikita.store.TaskStore

import java.time.LocalDate

class ToDoListManager {
    TaskStore taskStore
    ActionStore actionStore

    ToDoListManager(TaskStore taskStore, ActionStore actionStore) {
        this.taskStore = taskStore
        this.actionStore = actionStore
    }

    def getTasks() {
        return this.taskStore.tasks
    }

    def addTask(Task task) {
        if (!checkIntersection(task))
            this.taskStore.add(task)
        else
            println "Задача не добавлена. Пересечение времени с уже существующими задачами"
    }

    def addAction(String taskID, Action action) {
        if (!checkIntersection(taskID, action)) {
            if (withinTask(taskID, action)) {
                this.actionStore.addAction(taskID, action)
            } else {
                println "Действие выходит за пределы времени задачи"
            }
        } else {
            println "Действие не добавлено. Пересечение времени с уже существующими действиями в задаче"
        }
    }

    boolean checkTaskExist(String taskID) {
        return this.taskStore.tasks.any {it.id == taskID as int}
    }

    boolean checkActionExist(String actionID) {
        return this.actionStore.actions.values().any{ list ->
            list.any{ it.id == actionID as int}
        }
    }

    def getActions(String taskID) {
        return this.actionStore.getActions(taskID)
    }

    private boolean checkIntersection(Task newTask) {
        return this.taskStore.tasks.any{existing ->
            newTask.start.isBefore(existing.end) && newTask.end.isAfter(existing.start)
        }
    }

    private boolean withinTask(String taskID, Action action) {
        Task task = this.taskStore.tasks.find{ it.id == taskID as Integer }
        !action.start.isBefore(task.start) && !action.end.isAfter(task.end)
    }

    private boolean checkIntersection(String taskID, Action newAction) {
        def taskActions = this.actionStore.getActions(taskID)
        def result = taskActions.any{existing ->
            newAction.start.isBefore(existing.end) && newAction.end.isAfter(existing.start)
        }
        return result
    }

    def removeTask(String taskID) {
        Task task = this.taskStore.tasks.find {it.id == taskID as Integer}
        this.taskStore.delete(task)
        this.actionStore.deleteActionsByTask(taskID)
    }

    def removeAction(String actionID) {
        this.actionStore.deleteAction(actionID)
    }

    def getBusyTimeForDay(LocalDate day) {
        def startOfDay = day.atStartOfDay()
        def endOfDay = day.plusDays(1).atStartOfDay()
        def dailyTasks = this.taskStore.tasks.findAll { t ->
            !t.end.isBefore(startOfDay) && !t.start.isAfter(endOfDay)
        }.collect { t ->
            def s = t.start.isBefore(startOfDay) ? startOfDay : t.start
            def e = t.end.isAfter(endOfDay) ? endOfDay : t.end

            s = s.withSecond(0).withNano(0)
            e = e.withSecond(0).withNano(0)

            [start: s, end: e]
        }.sort { it.start }

        def merged = []
        dailyTasks.each { interval ->
            if (merged.isEmpty()) {
                merged << interval
            } else {
                def last = merged.last()
                if (!interval.start.isAfter(last.end)) {
                    last.end = last.end.isAfter(interval.end) ? last.end : interval.end
                } else {
                    merged << interval
                }
            }
        }
        return merged
    }
}
