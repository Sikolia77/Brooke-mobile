package ke.co.neatline.brookemapper.models

data class CommunityGroupsBody(
   val Name:String,
   val Country: String,
   val County: String,
   val Longitude: Double,
   val Latitude: Double,
   val SubCounty: String,
   val Ward: String,
   val Members: String,
   val Equines: String,
   val User: String
)