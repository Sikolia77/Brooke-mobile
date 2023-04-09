package ke.co.osl.brookemapper.models

data class AgrovetGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val ServiceArea: String,
   val User: String
)