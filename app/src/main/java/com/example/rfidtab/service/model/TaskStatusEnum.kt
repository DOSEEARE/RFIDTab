package com.example.rfidtab.service.model

object TaskStatusEnum {
    var sentToExecutor = 1
    var takenForExecution = 2
    var savedToLocal = 3
    var done = 4
    var createdEdited = 5
    var sentForRevision = 6

/*  1: Отправлено на исполнение
    2: Принято на исполнение
    3: Результаты сохранены локально (Отправить задание на проверку)
    4: Выполнено полностью (Принять от Исполнителя)
    5: Созданно (или отредактированно)
    6: Отправленно на доработку (такие же функции как у "Принято на исполнение")*/

}