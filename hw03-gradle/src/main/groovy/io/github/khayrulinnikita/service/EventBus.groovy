package io.github.khayrulinnikita.service

import io.github.khayrulinnikita.model.Event

class EventBus {
    def consumers = []

    def send(Event event) {
        consumers.each {
            it(event)
        }
    }

    def registerConsumer(Closure cl) {
        consumers.add(cl)
    }
}
