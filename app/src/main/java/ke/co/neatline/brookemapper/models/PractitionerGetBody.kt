package ke.co.neatline.brookemapper.models

data class PractitionerGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val Designation: String,
   val Contact: String,
   val User: String
)