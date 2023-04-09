package ke.co.osl.brookemapper.models

data class ChangePasswordBody(
    val Password: String,
    val NewPassword: String
)