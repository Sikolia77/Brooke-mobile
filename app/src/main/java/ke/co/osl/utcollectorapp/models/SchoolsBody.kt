package ke.co.osl.utcollectorapp.models

data class SchoolsBody(
   val Name:String,
   val Contact:String,
   val Patron: String,
   val ClubLevel: String,
   val County: String,
   val Longitude: Double,
   val Latitude: Double,
   val SubCounty: String,
   val Ward: String,
   val StudentsNo: String,
   val Activities: String,
   val User: String
)