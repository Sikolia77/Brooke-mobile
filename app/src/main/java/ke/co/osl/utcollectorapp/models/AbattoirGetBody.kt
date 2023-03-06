package ke.co.osl.utcollectorapp.models

data class AbattoirGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val Status: String,
   val User: String
)