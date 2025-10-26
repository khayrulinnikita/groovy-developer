package io.github.khayrulinnikita.store

import io.github.khayrulinnikita.model.Action

class ActionStore {
    Map<String, List<Action>> actions = [:]

    def addAction(String taskID, Action action) {
        (actions[taskID] ?: (actions[taskID] = [])).add(action)
    }

    def deleteActionsByTask(String taskID) {
        actions.remove(taskID)
    }

    def deleteAction(String actionID) {
        actions.each { task, list ->
            list.removeAll{ it.id == actionID as int }
        }
    }

    def getActions(String taskID) {
        return actions[taskID] ?: []
    }
}
