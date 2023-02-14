package ke.co.osl.utcollectorapp.models

data class CareclubGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val Contact:String,
   val Patron:String,
   val ClubLevel:String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val ServiceArea: String,
   val StudentsNo: String,
   val Activities: String,
   val User: String
)