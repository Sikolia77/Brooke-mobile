package ke.co.neatline.brookemapper.models

data class EquineOwnersGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val Country: String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val Contacts: String,
   val GroupName: String,
   val NumberofEquines: String,
   val User: String
)