class Cloze(question: String, answer: String): Card(question, answer) {
    override fun show(modificacion: Boolean) {
        print("\t$question (INTRO para ver respuesta) ")
        val time1 = System.currentTimeMillis()
        readLine()
        val time2 = System.currentTimeMillis()
        val exchange = question.substringAfter(" *").substringBefore("* ")
        val finalAnswer = question.replace(exchange, answer).replace("*", "")
        print("\t$finalAnswer")
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

    override fun toString(): String {
        return "cloze|$question|$answer|$date|$id|$easiness|$repetitions|$interval|$nextPracticeDate"
    }

    companion object {
        fun fromString(cad: String): Cloze {
            val trozos = cad.split("|")
            val question = trozos[1]
            val answer = trozos[2]
            val cloze = Cloze(question,answer)
            cloze.date = trozos[3]
            cloze.id = trozos[4]
            cloze.easiness = trozos[5].toDouble()
            cloze.repetitions = trozos[6].toInt()
            cloze.interval = trozos[7].toLong()
            cloze.nextPracticeDate = trozos[8]
            return cloze
        }
    }
}