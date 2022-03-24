package es.uam.eps.dadm.cards

import android.view.View
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*
import kotlin.math.max
import kotlin.math.roundToLong

open class Card(question: String, answer: String) {
    var question: String = question
    var answer: String = answer
    var quality: Int = 0
    var date: String = LocalDateTime.now().toString()
    var id: String = UUID.randomUUID().toString()
    var repetitions: Int = 0
    var interval: Long = 1L
    var nextPracticeDate: String = date
    var easiness: Double = 2.5
    var answerTimes: MutableList<Double> = mutableListOf<Double>()
    var answered: Boolean = false

    open fun show(modificacion: Boolean) {
        print("\t$question (INTRO para ver respuesta) ")
        val time1 = System.currentTimeMillis()
        readLine()
        val time2 = System.currentTimeMillis()
        print("\t$answer")
        if (modificacion) print(" (Teclea: -1 -> Otra vez // 0 -> Difícil // 3 -> Dudo // 5 -> Fácil) ")
        else print(" (Teclea: 0 -> Difícil // 3 -> Dudo // 5 -> Fácil) ")
        val quality = readLine()!!.toInt()
        if (((quality == 0 || quality == 3 || quality == 5) && !modificacion) ||
            (quality == -1 || quality == 0 || quality == 3 || quality == 5) && modificacion) {
            this.quality = quality
        }
        else {
            throw Exception("Número incorrecto")
        }
        val answerTime = (time2-time1)/1000.0
        answerTimes.add(answerTime)
    }

    fun update(currentDate: LocalDateTime) {
        if (quality == -1) quality = 0
        easiness = max(1.3, easiness + 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))
        if (quality < 3) repetitions = 0 else repetitions += 1
        if (repetitions <= 1)
            interval = 1
        else if (repetitions == 2)
            interval = 6
        else
            interval = (interval * easiness).roundToLong()
        nextPracticeDate = currentDate.plusDays(interval).toString()
    }

    fun details() {
        println("\teas = ${String.format("%.2f", easiness)} // rep = $repetitions // int = $interval // " +
                "next = ${nextPracticeDate.substring(0,4)}-${nextPracticeDate.substring(6,7)}-${nextPracticeDate.substring(8,10)}")
    }

    override fun toString(): String {
        return "card|$question|$answer|$date|$id|${easiness.toString()}|${repetitions.toString()}" +
                "|${interval.toString()}|$nextPracticeDate"
    }

    fun updateFromView(view: View) {
        quality = when(view.id) {
            R.id.easy_button -> 5
            R.id.doubt_button -> 3
            R.id.difficult_button -> 0
            else -> throw Exception("Unavailable quality")
        }
        update(LocalDateTime.now())
    }

    fun update_card(quality: Int) {
        this.quality = quality
        update(LocalDateTime.now())
    }

    fun isDue(date: LocalDateTime): Boolean {
        return nextPracticeDate <= date.toString()
    }

    companion object {
        fun fromString(cad: String): Card {
            val trozos = cad.split("|")
            var question = trozos[1]
            var answer = trozos[2]
            var card = Card(question,answer)
            card.date = trozos[3]
            card.id = trozos[4]
            card.easiness = trozos[5].toDouble()
            card.repetitions = trozos[6].toInt()
            card.interval = trozos[7].toLong()
            card.nextPracticeDate = trozos[8]
            card.answered = false
            return card
        }
    }
}
