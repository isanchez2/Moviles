class Definition(question: String, answer: String): Card(question, answer) {
    override fun show(modificacion: Boolean) {
        val len = answer.length
        var complete = "_"
        complete = complete.repeat(len)
        print("\t$question: $complete (INTRO para comprobar respuesta) ")
        val time1 = System.currentTimeMillis()
        val resp = readLine()!!.toString()
        val time2 = System.currentTimeMillis()
        if (resp == answer) {
            print("\tCorrecto!")
        }
        else {
            print("\tError! La respuesta era $answer")
        }
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
        return "definition|$question|$answer|$date|$id|$easiness|$repetitions|$interval|$nextPracticeDate"
    }

    companion object {
        fun fromString(cad: String): Definition {
            val trozos = cad.split("|")
            val question = trozos[1]
            val answer = trozos[2]
            val definition = Definition(question,answer)
            definition.date = trozos[3]
            definition.id = trozos[4]
            definition.easiness = trozos[5].toDouble()
            definition.repetitions = trozos[6].toInt()
            definition.interval = trozos[7].toLong()
            definition.nextPracticeDate = trozos[8]
            return definition
        }
    }
}