package io.github.khayrulinnikita.model

import java.time.LocalDateTime

class Task {
    static int counter = 0
    int id
    String name
    LocalDateTime start
    LocalDateTime end

    Task(String name, LocalDateTime start, LocalDateTime end) {
        this.id = ++counter
        this.name = name
        this.start = start
        this.end = end
    }

    String toString() {
        "${id}. Task \"${name}\": start time: ${start}, end time: ${end}"
    }

}
