import com.github.kinquirer.core.Choice
import com.github.kinquirer.kinquirer
import com.github.kinquirer.prompts.*
//import com.github.kinquirer.components.*
//import com.github.kinquirer.core.Choice
//import com.github.kinquirer.KInquirer
import java.math.BigDecimal

//
data class PizzaOrder(
    val isDelivery: Boolean,
    val phoneNumber: String,
    val size: String,
    val quantity: BigDecimal,
    val toppings: List<String>,
    val beverage: String,
    val comments: String,
)

fun main() = kinquirer {
    info("Hi, welcome to Kotlin's Pizza")
    val isDelivery: Boolean = promptConfirm("Is this for delivery?", default = false)
    val phoneNumber: String = promptInput(
        message = "What's your phone number?",
        filter = { s -> s.matches("\\d+".toRegex()) },
    )
    val size: String = promptList("What size do you need?", listOf("Large", "Medium", "Small"))
    val quantity: BigDecimal = promptInputNumber("How many do you need?")
    val toppings: List<String> = promptCheckboxObject(
        message = "What about the toppings?",
        choices = listOf(
            Choice("Pepperoni and cheese", "pepperonicheese"),
            Choice("All dressed", "alldressed"),
            Choice("Hawaiian", "hawaiian"),
        ),
    )
    val beverage: String = promptList("You also get a free 2L beverage", listOf("Pepsi", "7up", "Coke"))
    val comments: String = promptInput(
        message = "Any comments on your purchase experience?",
        hint = "Nope, all good!",
        default = "Nope, all good!",
    )

    val order = PizzaOrder(
        isDelivery = isDelivery,
        phoneNumber = phoneNumber,
        size = size,
        quantity = quantity,
        toppings = toppings,
        beverage = beverage,
        comments = comments,
    )

    info("====== Order receipt ======")
    info(order)
}

