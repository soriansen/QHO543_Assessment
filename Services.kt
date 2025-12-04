// Services.kt - Contains service/business logic classes

// Class representing the Sales Tracker
class SalesTracker {
    private val stations = mutableListOf<Station>()

    fun registerStation(station: Station) {
        stations.add(station)
    }

    fun recordSale(ticket: Ticket) {
        val station = ticket.getDestinationStation()
        station.addTakings(ticket.getPrice())
    }

    fun displayAllTakings() {
        println("\n--- Station Takings ---")
        var total = 0.0
        stations.forEach { station ->
            station.displayTakings()
            total += station.getTakings()
        }
        println("-".repeat(25))
        println("Total: £%.2f".format(total))
    }
}

// Class representing the Route Catalog
class RouteCatalog(private val originStation: Station) {
    private val routes = mutableListOf<Route>()

    fun addDestination(destination: Station, singlePrice: Double, returnPrice: Double): Station {
        routes.add(Route(originStation, destination, singlePrice, returnPrice))
        return destination
    }

    fun displayAllRoutes() {
        println("\n--- Available Destinations ---")
        routes.forEachIndexed { index, route ->
            route.displayPricing(index + 1)
        }
    }

    fun promptAndSelectRoute(): Route? {
        displayAllRoutes()
        print("\nSelect destination (1-${routes.size}): ")

        val choice = readLine()?.toIntOrNull()
        if (choice == null || choice !in 1..routes.size) {
            println("Invalid destination choice.")
            return null
        }

        return routes[choice - 1]
    }
}

// Class representing the Ticket Selector (handles ticket search/selection flow)
class TicketSelector(private val routeCatalog: RouteCatalog) {
    private var selectedTicket: Ticket? = null

    fun searchAndSelectTicket(): Ticket? {
        val route = routeCatalog.promptAndSelectRoute() ?: return null

        TicketType.displayOptions()
        val ticketType = TicketType.fromChoice(readLine()?.toIntOrNull() ?: 0)

        if (ticketType == null) {
            println("Invalid ticket type.")
            return null
        }

        val ticket = route.createTicket(ticketType)
        selectedTicket = ticket

        println("\n--- Ticket Found ---")
        ticket.display()
        println("\nUse 'Buy current ticket' option to purchase.")

        return ticket
    }

    fun getCurrentTicket(): Ticket? = selectedTicket

    fun hasTicketSelected(): Boolean = selectedTicket != null

    fun clearSelection() {
        selectedTicket = null
    }

    fun displayCurrentTicket() {
        val ticket = selectedTicket
        if (ticket == null) {
            println("\nNo ticket currently selected.")
        } else {
            println("\n--- Currently Selected Ticket ---")
            ticket.display()
        }
    }
}

// Class representing the Purchase Processor
class PurchaseProcessor(
    private val client: Client,
    private val salesTracker: SalesTracker,
    private val ticketSelector: TicketSelector
) {
    fun processPurchase(): Boolean {
        val ticket = ticketSelector.getCurrentTicket()

        if (ticket == null) {
            println("\nNo ticket selected. Please search for a ticket first.")
            return false
        }

        val result = client.processPayment(ticket.getPrice())

        return when (result) {
            is PaymentResult.InsufficientFunds -> {
                result.displayError()
                false
            }
            is PaymentResult.Success -> {
                salesTracker.recordSale(ticket)
                ticket.printPurchaseConfirmation()

                if (result.change > 0) {
                    println("Change returned: £%.2f".format(result.change))
                }

                ticketSelector.clearSelection()
                true
            }
        }
    }
}