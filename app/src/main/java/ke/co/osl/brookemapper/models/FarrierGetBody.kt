package ke.co.osl.brookemapper.models

data class FarrierGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val Contact:String,
   val Group:String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val ServiceArea: String,
   val User: String
)