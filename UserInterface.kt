// UserInterface.kt - Contains UI-related classes

// Class representing the Main Menu
class MainMenu(private val stationName: String, private val client: Client) {
    fun display() {
        println("\n--- Main Menu ---")
        println("1. Search for a ticket")
        print("2. Insert money (")
        client.displayBalance()
        println(")")
        println("3. Buy current ticket")
        println("4. View station takings")
        println("5. Exit")
        print("Choose an option: ")
    }

    fun getUserChoice(): Int {
        return readLine()?.toIntOrNull() ?: -1
    }

    fun displayWelcome() {
        println("=".repeat(50))
        println("Welcome to $stationName Train Ticket Machine")
        println("=".repeat(50))
    }

    fun displayGoodbye() {
        println("Thank you for using the ticket machine. Goodbye!")
    }

    fun displayInvalidOption() {
        println("Invalid option. Please try again.")
    }
}