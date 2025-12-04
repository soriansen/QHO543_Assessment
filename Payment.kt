// Payment.kt - Contains payment-related classes

// Sealed class for payment results
sealed class PaymentResult {
    data class Success(val change: Double) : PaymentResult()
    data class InsufficientFunds(
        val required: Double,
        val available: Double,
        val shortfall: Double
    ) : PaymentResult() {
        fun displayError() {
            println("\nInsufficient funds!")
            println("Ticket price: £%.2f".format(required))
            println("Current balance: £%.2f".format(available))
            println("Please insert £%.2f more.".format(shortfall))
        }
    }
}

// Class representing the Client (handles money)
class Client {
    private var balance: Double = 0.0

    fun promptAndInsertMoney() {
        print("\nEnter amount to insert (£): ")
        val amount = readLine()?.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            println("Invalid amount.")
            return
        }

        balance += amount
        println("£%.2f inserted. Current balance: £%.2f".format(amount, balance))
    }

    fun getBalance(): Double = balance

    fun canAfford(amount: Double): Boolean = balance >= amount

    fun processPayment(amount: Double): PaymentResult {
        if (!canAfford(amount)) {
            return PaymentResult.InsufficientFunds(
                required = amount,
                available = balance,
                shortfall = amount - balance
            )
        }

        balance -= amount
        val change = balance
        balance = 0.0
        return PaymentResult.Success(change)
    }

    fun displayBalance() {
        print("Current balance: £%.2f".format(balance))
    }
}