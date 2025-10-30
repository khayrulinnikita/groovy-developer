import io.github.khayrulinnikita.model.Action
import io.github.khayrulinnikita.model.Task
import spock.lang.Specification

import java.time.LocalDateTime

class ActionTest extends Specification {
    def "Создание action"() {
        when:
        Action testAction = new Action("test action", LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2))

        then:
        testAction.id == 1
    }
}
