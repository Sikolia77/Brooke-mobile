package ke.co.neatline.brookemapper.models

data class CommunityGroupsGetBody(
   val ID: String,
   val ObjectID: String,
   val Name:String,
   val County: String,
   val SubCounty: String,
   val Ward:String,
   val Members: String,
   val Equines: String,
   val User: String
)