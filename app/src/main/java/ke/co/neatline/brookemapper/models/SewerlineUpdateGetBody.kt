package ke.co.neatline.brookemapper.models

data class SewerlineUpdateGetBody(
  val ID: String,
  val ObjectID: String,
  val Material:String,
  val Type:String,
  val Route:String,
  val Zone:String,
  val SchemeName: String,
  val Size:String,
  val Status:String,
  val Remarks:String,
)