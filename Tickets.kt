// Tickets.kt - Contains classes like ticket types, stations, train routes

// Enum for ticket types
enum class TicketType {
    SINGLE,
    RETURN;

    companion object {
        fun fromChoice(choice: Int): TicketType? {
            return when (choice) {
                1 -> SINGLE
                2 -> RETURN
                else -> null
            }
        }

        fun displayOptions() {
            println("\nTicket Type:")
            println("1. Single")
            println("2. Return")
            print("Choose ticket type: ")
        }
    }
}

// Class representing a Train Station
class Station(val name: String) {
    private var takings: Double = 0.0

    fun addTakings(amount: Double) {
        takings += amount
    }

    fun getTakings(): Double = takings

    fun displayTakings() {
        println("${name}: £%.2f".format(takings))
    }

    override fun toString(): String = name
}

// Class representing a Route between two stations
class Route(
    val origin: Station,
    val destination: Station,
    private val singlePrice: Double,
    private val returnPrice: Double
) {
    fun getPrice(ticketType: TicketType): Double {
        return when (ticketType) {
            TicketType.SINGLE -> singlePrice
            TicketType.RETURN -> returnPrice
        }
    }

    fun displayPricing(index: Int) {
        println("$index. ${destination.name} - Single: £%.2f, Return: £%.2f"
            .format(singlePrice, returnPrice))
    }

    fun createTicket(ticketType: TicketType): Ticket {
        return Ticket(this, ticketType)
    }
}

// Class representing a Ticket
class Ticket(
    private val route: Route,
    private val type: TicketType
) {
    private val price: Double = route.getPrice(type)

    fun getPrice(): Double = price

    fun getDestinationStation(): Station = route.destination

    fun display() {
        println("***")
        println(route.origin.name)
        println("to")
        println(route.destination.name)
        println("Price: %.2f [%s]".format(price, type))
        println("***")
    }

    fun printPurchaseConfirmation() {
        println("\n--- TICKET PURCHASED ---")
        display()
        println("\nThank you for your purchase!")
    }
}