fun main() {
    print("Introduce tu nombre de usuario: ")
    val user = readLine()!!.toString()
    val game = Game()
    println("\tBienvenido $user!!")
    try {
        while (true) {
            println("MENÚ INICIAL")
            print("1. Añadir mazo\n" +
                    "2. Listar mazos\n" +
                    "3. Eliminar mazo\n" +
                    "4. Jugar\n" +
                    "5. Leer mazos de fichero\n" +
                    "6. Escribir mazos en fichero\n" +
                    "7. Salir\n")
            print("Elige una opción: ")
            val option = readLine()!!.toString()
            when(option) {
                "1" -> game.addDeck()
                "2" -> game.listDecks()
                "3" -> game.deleteDeck()
                "4" -> game.playDeck()
                "5" -> game.readDecks("data/decks.txt")
                "6" -> game.writeDecks("data/decks.txt")
                "7" -> return
                else -> throw Exception("Opción incorrecta")
            }
            println()
        }
    } catch (e: Exception) {
        println("Opción incorrecta")
    }
}