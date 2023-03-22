//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.{SparkConf, SparkContext}
//
//case class student(name:String,marks:Int,school:String, district:String, emailid:String)
//object dataInLines {
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setMaster("local[2]").setAppName("readLine")
//    val sc = new SparkContext(conf)
//    val ssc = new StreamingContext(sc,Seconds(30))
//    val lines = ssc.socketTextStream("localhost",9900)
//    val line = lines.map(x=>student(x.split(",")(0),x.split(",")(1).toInt,x.split(",")(2),x.split(",")(3),x.split(",")(4)))
//    val filter = line.filter(_.marks > 500)
//    filter.print
//    ssc.start()
//    ssc.awaitTermination()
//
//  }}
