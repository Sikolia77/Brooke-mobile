package ke.co.osl.brookemapper.models

data class EquineOwnersBody(
   val Name:String,
   val County: String,
   val Longitude: Double,
   val Latitude: Double,
   val SubCounty: String,
   val Ward: String,
   val Contacts: String,
   val GroupName: String,
   val NumberofEquines: String,
   val User: String
)