package io.github.khayrulinnikita.model

import java.time.LocalDateTime

class StartActionEvent implements Event {
    LocalDateTime timestamp
    String message
    String type = "Action started"

    StartActionEvent(LocalDateTime timestamp, Action action) {
        this.timestamp = timestamp
        this.message = "Start action \"${action.name}\""
    }

    String toString() {
        "Event \"${type}\" started at ${timestamp}"
    }
}
