import com.github.kinquirer.prompts.*
import com.github.kinquirer.kinquirer
import com.varabyte.kotter.foundation.text.text
import java.math.BigDecimal

fun main() = kinquirer {
    // Confirm
    val isDelivery: Boolean = promptConfirm(message = "Is this for delivery?", default = false)
    println("Is Delivery: $isDelivery")

    // Input
    val comments: String = promptInput(message = "Any comments on your purchase experience?")
    println("Comments: $comments")

    // Input Numbers
    val quantity: BigDecimal = promptInputNumber(message = "How many do you need?")
    println("Quantity: $quantity")

    // Input Password
    val password: String = promptInputPassword(message = "Enter Your Password:", hint = "password")
    println("Password: $password")

    // Input Password Masked
    val passwordMasked: String = promptInputPassword(
        message = "Enter Your Password:",
        hint = "password",
        mask = 'x'
    )
    println("Password: $passwordMasked")

    // List
    val size: String =
        promptList(message = "What size do you need?", choices = listOf("Large", "Medium", "Small"))
    println("Size: $size")

    // List View Options
    val continent: String = promptList(
        message = "Select a continent:",
        choices = listOf(
            "Asia",
            "Africa",
            "Europe",
            "North America",
            "South America",
            "Australia",
            "Antarctica",
        ),
        hint = "press Enter to pick",
        pageSize = 3,
        viewOptions = ListViewOptions(
            questionMarkPrefix = { text("🌍") },
            cursor = { text(" 😎 ") },
            nonCursor = { text("    ") },
        )
    )
    println("Continent: $continent")

    // Checkbox
    val toppings: List<String> = promptCheckbox(
        message = "What about the toppings?",
        choices = listOf(
            "Pepperoni and cheese",
            "All dressed",
            "Hawaiian",
        ),
    )
    println("Toppings: $toppings")

    // Checkbox View Options
    val colors: List<String> = promptCheckbox(
        message = "Which colors do you prefer?",
        choices = listOf(
            "Red",
            "Green",
            "Blue",
            "Yellow",
            "Black",
            "White",
        ),
        hint = "pick a color using spacebar",
        maxNumOfSelection = 3,
        minNumOfSelection = 2,
        pageSize = 3,
        viewOptions = CheckboxViewOptions(
            questionMarkPrefix = { text("❓") },
            cursor = { text(" 👉 ") },
            nonCursor = { text("    ") },
            checked = { text("✅ ") },
            unchecked = { text("○ ") },
        )
    )
    println("Colors: $colors")
}
