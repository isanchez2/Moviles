package es.uam.eps.dadm.cards

class Game {
    private var decks = mutableListOf<Deck>()
    /*
    fun listDecks() {
        if (decks.isEmpty()) {
            println("\tNo hay mazos disponibles")
            return
        }
        for (deck in decks) {
            println("\t[${deck.name}: tarjetas disponibles = ${deck.cards.size}]")
        }
    }

    fun addDeck() {
        print("\tTeclea el idioma del mazo: ")
        val idioma = readLine()!!.toString()
        for (d in decks) {
            if (idioma == d.name) {
                println("\tEl mazo ya existe")
                return
            }
        }
        val deck = Deck(idioma)
        decks.add(deck)
        println("\tMazo añadido correctamente")
    }

    fun deleteDeck() {
        print("\tTeclea el nombre del mazo a eliminar: ")
        val deleted = readLine()!!.toString()
        if (deleted == "") {
            println("No se ha tecleado ningún nombre")
            return
        }
        for (deck in decks) {
            if (deck.name == deleted) {
                decks.remove(deck)
                println("\tMazo eliminado")
                return
            }
        }
        println("\tMazo no encontrado")
    }

    fun playDeck() {
        if (decks.isEmpty()) {
            println("\tNo hay mazos disponibles. Debes crear uno primero")
            return
        }
        println("\tMazos disponibles para jugar:")
        for ((i, deck) in decks.withIndex()) {
            println("\t\t$i -> ${deck.name}")
        }
        print("Teclea el número de mazo al que quieres jugar: ")
        val num = readLine()!!.toInt()
        println()
        val deck = decks[num]
        while (true) {
            println("Mazo ${deck.name}")
            print("1. Añadir tarjeta\n" +
                    "2. Lista de tarjetas\n" +
                    "3. Eliminar tarjeta\n" +
                    "4. Simulación\n" +
                    "5. Leer tarjetas de fichero\n" +
                    "6. Escribir tarjetas en fichero\n" +
                    "7. Visualizar estadísticas de mazo\n" +
                    "8. Salir\n")
            print("Elige una opción: ")
            when(readLine()!!.toInt()) {
                1 -> deck.addCard()
                2 -> deck.listCards()
                3 -> deck.deleteCard()
                4 -> {
                    print("\tIntroduce el número de días para simular: ")
                    val period = readLine()!!.toInt()
                    print("\t¿Quieres activar la repetición de tarjetas al finalizar cada día? (si/no) ")
                    when (readLine()!!.toString()) {
                        "si" -> deck.simulate(period, true)
                        "no" -> deck.simulate(period, false)
                        else -> println("\tOpción incorrecta")
                    }
                }
                5 -> deck.readCards("data/cards_${deck.name}.txt")
                6 -> deck.writeCards("data/cards_${deck.name}.txt")
                7 -> deck.showStatistics()
                8 -> return
                else -> println("Opción incorrecta")
            }
            println()
        }
    }

    fun writeDecks(name: String) {
        if (decks.isEmpty()) {
            println("\tNo hay mazos para guardar")
            return
        }
        val file = File(name)
        file.printWriter().use { out ->
            decks.forEach {
                out.println(it.toString())
            }
        }
        println("\tLos mazos se han escrito en fichero correctamente")
    }

    fun readDecks(name: String) {
        try {
            val lines = File(name).readLines()
            if (lines.isEmpty()) {
                println("\tEl fichero está vacío")
                return
            }
            for (line in lines) {
                decks.add(Deck.fromString(line))
            }
            println("\tDatos cargados correctamente")
        } catch (e: FileNotFoundException) {
            println("\tNo se ha encontrado el fichero")
        }
    } */
}