package ke.co.neatline.brookemapper.models

data class WaterpipesDataUpdateBody(
  val LineName:String,
  val Material:String,
  val Intake: String,
  val Type:String,
  val Function:String,
  val DMA:String,
  val Route:String,
  val schemeName: String,
  val Zone: String,
  val Size:String,
  val Status:String,
  val Remarks:String,
  val User: String
)