package io.github.khayrulinnikita.view

import io.github.khayrulinnikita.model.Action
import io.github.khayrulinnikita.model.Event
import io.github.khayrulinnikita.model.Task
import io.github.khayrulinnikita.service.EventBus
import io.github.khayrulinnikita.service.ToDoListManager

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ConsoleView {
    ToDoListManager manager
    def timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm")
    def dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    def menuOptions = [
            "Показать список всех задач",
            "Создать задачу",
            "Удалить задачу",
            "Показать список задач на выбранный день",
            "Показать количество задач на выбранный день",
            "Показать список действий по выбранной задаче",
            "Создать действие по задаче",
            "Удалить действие по задаче",
            "Показать время занятости на день",
            "Выход"
    ]

    ConsoleView(ToDoListManager manager, EventBus eventBus) {
        this.manager = manager
        eventBus.registerConsumer(this.&consumeEvent)
    }

    static def consumeEvent(Event event) {
        println event
    }

    def showMenuOptions() {
        println("Options:")
        menuOptions.eachWithIndex { option, index ->
            println "${index + 1}. ${option}" }
    }

    def createTask() {
        println "Введите задачу в формате: 'name YYYY-MM-DD:HH-MM YYYY-MM-DD:HH-MM'\nДля возврата в главное меню нажмите enter"
        String input = System.in.newReader().readLine().trim()
        if (!input) {
            println "Возврат в главное меню"
            return
        }
        def parts = input.split(" ")
        if (parts.size() != 3) {
            println "Неверный формат ввода\nВозврат в главное меню"
            return
        }

        def (name, dateStartStr, dateEndStr) = parts
        try {
            LocalDateTime dateStartTime = LocalDateTime.parse(dateStartStr, this.timeFormatter)
            LocalDateTime dateEndTime = LocalDateTime.parse(dateEndStr, this.timeFormatter)
            if (dateEndTime.isBefore(dateStartTime)) {
                println "Неверный промежуток времении для задачи"
                return
            }
            Task task = new Task(name, dateStartTime, dateEndTime)
            this.manager.addTask(task)
        } catch (DateTimeParseException e) {
            println "Неверный формат времени. Должен быть YYYY-MM-DD:HH-MM"
        }
    }

    def printTasks() {
        this.manager.getTasks().sort {it.start}.each {
            println it
        }
    }

    def printTasksForDay() {
        println "Введите дату в формате: 'YYYY-MM-DD'\nДля возврата в главное меню нажмите enter"
        String input = System.in.newReader().readLine().trim()
        if (!input) {
            println "Возврат в главное меню"
            return
        }
        def day = LocalDate.parse(input, this.dayFormatter)
        this.manager.tasks.findAll{it.start.toLocalDate() == day}.sort{it.start}.each {
            println it
        }
    }

    def printTasksCountForDay() {
        println "Введите дату в формате: 'YYYY-MM-DD'\nДля возврата в главное меню нажмите enter"
        String input = System.in.newReader().readLine().trim()
        if (!input) {
            println "Возврат в главное меню"
            return
        }
        def day = LocalDate.parse(input, this.dayFormatter)
        def count = this.manager.tasks.count{it.start.toLocalDate() == day}
        println count
    }

    def printActionsForTask() {
        printTasks()
        println "Укажите ID задачи"
        String input = System.in.newReader().readLine().trim()
        if (this.manager.checkTaskExist(input)) {
            def actions = this.manager.getActions(input)
            if (actions.size() == 0) {
                println "По выбранной задаче нет запланированных действий"
            } else {
                actions.each {
                    println it
                }
            }
        } else {
            println "Задача с ID ${input} не найдена"
        }
    }

    def createAction() {
        printTasks()
        println "Укажите ID задачи"
        String taskID = System.in.newReader().readLine().trim()
        if (this.manager.checkTaskExist(taskID)) {
            println "Введите действие в формате: 'name YYYY-MM-DD:HH-MM YYYY-MM-DD:HH-MM'\nДля возврата в главное меню нажмите enter"
            String input = System.in.newReader().readLine().trim()
            if (!input) {
                println "Возврат в главное меню"
                return
            }
            def parts = input.split(" ")
            if (parts.size() != 3) {
                println "Неверный формат ввода\nВозврат в главное меню"
                return
            }
            def (name, dateStartStr, dateEndStr) = parts
            try {
                LocalDateTime dateStartTime = LocalDateTime.parse(dateStartStr, this.timeFormatter)
                LocalDateTime dateEndTime = LocalDateTime.parse(dateEndStr, this.timeFormatter)
                if (dateEndTime.isBefore(dateStartTime)) {
                    println "Неверный промежуток времении для действия"
                    return
                }
                Action action = new Action(name, dateStartTime, dateEndTime)
                this.manager.addAction(taskID, action)
            } catch (DateTimeParseException e) {
                println "Неверный формат времени. Должен быть YYYY-MM-DD:HH-MM"
            }
        } else {
            println "Задача с ID ${taskID} не найдена"
        }
    }
    def removeTask() {
        printTasks()
        println "Укажите ID задачи"
        String taskID = System.in.newReader().readLine().trim()
        if (this.manager.checkTaskExist(taskID)) {
            this.manager.removeTask(taskID)
        } else {
            println "Задача с ID ${taskID} не найдена"
        }
    }

    def removeAction() {
        printActionsForTask()
        String actionID = System.in.newReader().readLine().trim()
        if (this.manager.checkActionExist(actionID)) {
            this.manager.removeAction(actionID)
        } else {
            println "Действие с ID ${actionID} не найдено"
        }

    }

    def getBusyTimeForDay() {
        println "Введите дату в формате: 'YYYY-MM-DD'\nДля возврата в главное меню нажмите enter"
        String input = System.in.newReader().readLine().trim()
        if (!input) {
            println "Возврат в главное меню"
            return
        }
        def day = LocalDate.parse(input, this.dayFormatter)
        println this.manager.getBusyTimeForDay(day)
    }

    def dialogMenu() {
        while (true) {
            showMenuOptions()
            String choice = System.in.newReader().readLine()

            switch (choice) {
                case '1':
                    printTasks()
                    break
                case '2':
                    createTask()
                    break
                case '3':
                    removeTask()
                    break
                case '4':
                    printTasksForDay()
                    break
                case '5':
                    printTasksCountForDay()
                    break
                case '6':
                    printActionsForTask()
                    break
                case '7':
                    createAction()
                    break
                case '8':
                    removeAction()
                    break
                case '9':
                    getBusyTimeForDay()
                    break
                case '10':
                    println "Выход"
                    System.exit(0)
                    break
                default:
                    println "Некорректный выбор"
            }
        }
    }
}
