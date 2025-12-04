// TicketMachine.kt - Main coordinator class

// Main Ticket Machine class - coordinates between objects
class TicketMachine(originStationName: String) {
    private val originStation = Station(originStationName)
    private val client = Client()
    private val salesTracker = SalesTracker()
    private val routeCatalog = RouteCatalog(originStation)
    private val ticketSelector = TicketSelector(routeCatalog)
    private val purchaseProcessor = PurchaseProcessor(client, salesTracker, ticketSelector)
    private val mainMenu = MainMenu(originStationName, client)

    init {
        setupRoutes()
    }

    private fun setupRoutes() {
        val destinations = listOf(
            Triple("London", 25.50, 45.00),
            Triple("Manchester", 35.00, 60.00),
            Triple("Birmingham", 20.00, 35.00),
            Triple("Liverpool", 30.00, 52.00),
            Triple("Edinburgh", 50.00, 85.00),
            Triple("Bristol", 22.00, 38.00)
        )

        destinations.forEach { (name, single, ret) ->
            val station = routeCatalog.addDestination(Station(name), single, ret)
            salesTracker.registerStation(station)
        }
    }

    fun start() {
        mainMenu.displayWelcome()

        var running = true
        while (running) {
            mainMenu.display()

            when (mainMenu.getUserChoice()) {
                1 -> ticketSelector.searchAndSelectTicket()
                2 -> client.promptAndInsertMoney()
                3 -> purchaseProcessor.processPurchase()
                4 -> salesTracker.displayAllTakings()
                5 -> {
                    mainMenu.displayGoodbye()
                    running = false
                }
                else -> mainMenu.displayInvalidOption()
            }
        }
    }
}