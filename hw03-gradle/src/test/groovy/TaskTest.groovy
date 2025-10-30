import io.github.khayrulinnikita.model.Task
import spock.lang.Specification

import java.time.LocalDateTime

class TaskTest extends Specification {
    def "Создание task"() {
        when:
        Task testTask = new Task("test task", LocalDateTime.now(), LocalDateTime.now().plusHours(2))

        then:
        testTask.id == 1
    }
}
