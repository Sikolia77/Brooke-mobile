package ke.co.osl.brookemapper.models

data class PractitionerBody(
   val Name:String,
   val County: String,
   val Longitude: Double,
   val Latitude: Double,
   val SubCounty: String,
   val Ward: String,
   val Designation: String,
   val Contact: String,
   val User: String
)