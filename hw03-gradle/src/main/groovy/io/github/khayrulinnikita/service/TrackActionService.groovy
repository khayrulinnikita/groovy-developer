package io.github.khayrulinnikita.service

import io.github.khayrulinnikita.model.Action
import io.github.khayrulinnikita.model.Event
import io.github.khayrulinnikita.model.StartActionEvent
import io.github.khayrulinnikita.store.ActionStore

import java.time.LocalDateTime

class TrackActionService {
    EventBus eventBus
    ActionStore actionStore

    TrackActionService(EventBus eventBus, ActionStore actionStore) {
        this.eventBus = eventBus
        this.actionStore = actionStore
    }

    def onSchedule() {
        while (true){
            def currentTime = LocalDateTime.now()
            List<Action> startedActions = (List<Action>) this.actionStore.actions.values().flatten().findAll {
                !it.isEvented && it.start.isBefore(currentTime)
            }

            startedActions.each {
                StartActionEvent event = new StartActionEvent(currentTime, it)
                produce(event)
                it.isEvented()
            }
            sleep(5000)
        }
    }


    def produce(Event event) {
        eventBus.send(event)
    }
}
