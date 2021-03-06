package com.example.rfidtab.service.model.enums

object TaskStatusEnum {
    var sentToExecutor = 1
    var takenForExecution = 2
    var savedToLocal = 3
    var done = 4
    var createdEdited = 5
    var sentForRevision = 6


    var sentToExecutorString = "Отправлено на исполнение"
    var takenForExecutionString = "Принято на исполнение"
    var savedToLocalString = "Результаты сохранены локально"
    var doneString = "Выполнено полностью"
    var createdEditedString = "Созданно"
    var sentForRevisionString = "Отправленно на доработку"

/*  1: Отправлено на исполнение
    2: Принято на исполнение
    3: Результаты сохранены локально (Отправить задание на проверку)
    4: Выполнено полностью (Принять от Исполнителя)
    5: Созданно (или отредактированно)
    6: Отправленно на доработку (такие же функции как у "Принято на исполнение")*/

}