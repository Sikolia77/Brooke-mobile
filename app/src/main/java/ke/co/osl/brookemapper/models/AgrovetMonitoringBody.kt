package ke.co.osl.brookemapper.models

data class AgrovetMonitoringBody(
   val Name:String,

   val Longitude: Double,
   val Latitude: Double,
   val County: String,
   val Town: String,
   val Unit: String,
   val Location: String,
   val Date: String,
   val Quater: String,
   val Project: String,
)