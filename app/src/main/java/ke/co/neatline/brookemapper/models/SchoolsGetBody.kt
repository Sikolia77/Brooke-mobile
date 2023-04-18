package ke.co.neatline.brookemapper.models

data class SchoolsGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val Contact:String,
   val Patron: String,
   val ClubLevel: String,
   val Country: String,
   val County: String,
   val SubCounty: String,
   val Ward: String,
   val StudentsNo: String,
   val ClubActivities: String,
   val User: String
)