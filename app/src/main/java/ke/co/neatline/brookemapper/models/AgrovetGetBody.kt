package ke.co.neatline.brookemapper.models

data class AgrovetGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val Country: String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val ServiceArea: String,
   val User: String
)