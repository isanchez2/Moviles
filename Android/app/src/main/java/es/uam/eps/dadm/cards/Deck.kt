package es.uam.eps.dadm.cards

import java.util.*
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Deck(name: String) {
    var name: String = name
    var id: String = UUID.randomUUID().toString()
    var cards = mutableListOf<Card>()
    var forgottenCards: Int = 0
    var firstAnswerCorrect: Int = 0

    fun addCard() {
        println("Añadiendo tarjeta al mazo ${this.name}:")
        print("\tTeclea el tipo (0 -> es.uam.eps.dadm.cards.Card // 1 -> es.uam.eps.dadm.cards.Cloze // 2 -> es.uam.eps.dadm.cards.Definition): ")
        val tipo = readLine()!!.toInt()
        if (tipo !in 0 .. 2) {
            println("Tipo no admitido")
            return
        }
        print("\tTeclea la pregunta: ")
        val question = readLine()!!.toString()
        if (question == "") {
            println("Tarjeta vacía")
            return
        }
        print("\tTeclea la respuesta: ")
        val answer = readLine()!!.toString()
        if (answer == "") {
            println("Tarjeta vacía")
            return
        }
        if (tipo == 0) {
            var card = Card(question, answer)
            cards.add(card)
        }
        else if (tipo == 1) {
            if (!question.contains("*")) {
                println("La tarjeta creada no es de tipo es.uam.eps.dadm.cards.Cloze")
                return
            }
            var cloze = Cloze(question, answer)
            cards.add(cloze)
        }
        else if (tipo == 2) {
            var definition = Definition(question, answer)
            cards.add(definition)
        }
        println("\tTarjeta añadida correctamente")
    }

    fun listCards() {
        if (cards.isEmpty() == true) {
            println("\tLista de tarjetas vacía")
            return
        }
        for (card in cards) {
            println("\t${card.question} -> ${card.answer}")
        }
    }

    fun deleteCard() {
        print("\tTeclea la pregunta de la tarjeta a eliminar: ")
        val deleted = readLine()!!.toString()
        if (deleted == "") {
            println("Tarjeta vacía")
            return
        }
        for (card in cards) {
            if (card.question == deleted) {
                cards.remove(card)
                println("\tTarjeta eliminada")
                return
            }
        }
        println("\tTarjeta no encontrada")
    }

    fun simulate(period: Int, modificacion: Boolean) {
        if (cards.isEmpty()) {
            println("\tPara simular primero debes añadir tarjetas")
            return
        }
        println("Simulación del mazo $name:")
        var fechaSim = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for (day in 1 .. period) {
            var repetir = mutableListOf<Card>()
            println("Fecha: ${fechaSim.format(formatter)}")
            for (card in cards) {
                var nextdate = LocalDateTime.of(card.nextPracticeDate.substring(0,4).toInt(),
                    card.nextPracticeDate.substring(5,7).toInt(), card.nextPracticeDate.substring(8,10).toInt(),
                    card.nextPracticeDate.substring(11,13).toInt(), card.nextPracticeDate.substring(14,16).toInt(),
                    card.nextPracticeDate.substring(17,19).toInt(), 0)
                if ((fechaSim.dayOfYear == nextdate.dayOfYear) && (fechaSim.year == nextdate.year)) {
                    try {
                        card.show(modificacion)
                    }
                    catch (e: Exception) {
                        println("Simulación abortada")
                        return
                    }
                    if (day == 1 && card.quality == 5) firstAnswerCorrect++
                    if (modificacion == true && card.quality == -1) {
                        repetir.add(card)
                        forgottenCards++
                    }
                    card.update(nextdate)
                    card.details()
                }
            }
            if (modificacion == true) {
                for (cardRep in repetir) {
                    cardRep.show(modificacion)
                    cardRep.details()
                }
            }
            val porcentaje = (day.toDouble()/period.toDouble())*100
            var progress = "|"
            var missing = "-"
            progress = progress.repeat(porcentaje.toInt())
            missing = missing.repeat(100-porcentaje.toInt())
            println("Porcentaje de simulación completado: ${String.format("%.2f", porcentaje)}%")
            println("[$progress$missing]")
            fechaSim = fechaSim.plusDays(1)
        }
    }

    fun showStatistics() {
        var numCards = 0
        var numClozes = 0
        var numDefinitions = 0
        var regexCard = "^card.*".toRegex()
        var regexCloze = "^cloze.*".toRegex()
        var regexDefinition = "^definition.*".toRegex()
        var totalAnswerTime = 0.0
        var easinessCard = 0.0
        var easinessCloze = 0.0
        var easinessDefinition = 0.0
        for (card in cards) {
            if (regexCard.find(card.toString()) != null) {
                easinessCard += card.easiness
                numCards++
            }
            else if (regexCloze.find(card.toString()) != null) {
                easinessCloze += card.easiness
                numClozes++
            }
            else if (regexDefinition.find(card.toString()) != null) {
                easinessDefinition += card.easiness
                numDefinitions++
            }
            var medAnswerTime = (card.answerTimes.sum()/card.answerTimes.size).toDouble()
            totalAnswerTime += medAnswerTime
        }
        println("----------------------------------------------------------------------------\n" +
                "|                               ESTADÍSTICAS                               |\n" +
                "----------------------------------------------------------------------------")
        println("Número de cartas totales en mazo $name: ${cards.size}")
        println("\t - Cards: $numCards")
        println("\t - Clozes: $numClozes")
        println("\t - Definitions: $numDefinitions")
        println("Tiempo medio de respuesta del mazo $name: ${String.format("%.2f", totalAnswerTime/cards.size)} segundos")
        println("Número de tarjetas repetidas: $forgottenCards")
        println("Número de tarjetas acertadas a la primera: $firstAnswerCorrect")
        var maxEasiness = maxOf(easinessCard, easinessCloze, easinessDefinition)
        print("Categoría más aprendida: ")
        if (maxEasiness == easinessCard) println("es.uam.eps.dadm.cards.Card")
        else if (maxEasiness == easinessCloze) println("es.uam.eps.dadm.cards.Cloze")
        else if (maxEasiness == easinessDefinition) println("es.uam.eps.dadm.cards.Definition")
        var minEasiness = minOf(easinessCard, easinessCloze, easinessDefinition)
        print("Categoría menos aprendida: ")
        if (minEasiness == easinessCard) println("es.uam.eps.dadm.cards.Card")
        else if (minEasiness == easinessCloze) println("es.uam.eps.dadm.cards.Cloze")
        else if (minEasiness == easinessDefinition) println("es.uam.eps.dadm.cards.Definition")
    }

    fun writeCards(name: String) {
        if (cards.isEmpty()) {
            println("\tNo hay tarjetas para guardar")
            return
        }
        val file = File(name)
        file.printWriter().use { out ->
            cards.forEach {
                out.println(it.toString())
            }
        }
        println("\tLas tarjetas se han escrito en fichero correctamente")
    }

    fun readCards(name: String) {
        try {
            val lines = File(name).readLines()
            if (lines.size == 0) {
                println("\tEl fichero está vacío")
                return
            }
            for (line in lines) {
                var regexCard = "^card.*".toRegex()
                var regexCloze = "^cloze.*".toRegex()
                var regexDefinition = "^definition.*".toRegex()
                if (regexCard.find(line) != null) {
                    cards.add(Card.fromString(line))
                }
                if (regexCloze.find(line) != null) {
                    cards.add(Cloze.fromString(line))
                }
                if (regexDefinition.find(line) != null) {
                    cards.add(Definition.fromString(line))
                }
            }
            println("\tDatos cargados correctamente")
        } catch (e: FileNotFoundException) {
            println("\tNo se ha encontrado el fichero")
        }
    }

    override fun toString(): String {
        return "$name|$id|${cards.size}"
    }

    companion object {
        fun fromString(cad: String): Deck {
            val trozos = cad.split("|")
            val name = trozos[0]
            val deck = Deck(name)
            deck.id = trozos[1]
            return deck
        }
    }
}