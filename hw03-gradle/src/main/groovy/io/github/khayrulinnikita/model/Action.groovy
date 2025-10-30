package io.github.khayrulinnikita.model

import java.time.LocalDateTime

class Action {
    static int counter = 0
    int id
    String name
    LocalDateTime start
    LocalDateTime end
    private boolean isEvented

    Action(String name, LocalDateTime start, LocalDateTime end) {
        this.id = ++counter
        this.name = name
        this.start = start
        this.end = end
    }

    String toString() {
        "${id}. Action '${name}': start time: ${start}, end time ${end}"
    }

    def isEvented() {
        this.isEvented = true
    }
}
