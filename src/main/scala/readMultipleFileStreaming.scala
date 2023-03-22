//
//import org.apache.commons.cli.Options
//
//import java.util.UUID
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.{SparkConf, SparkContext}
//
//case class student(name:String,marks:Int,school:String, district:String, emailid:String)
//
//object student {
//  def func(line:String):Option[student] = {
//    val data = line.split(",")
//    if (data.length == 5) {
//     Some(student(data(0),Integer.parseInt(data(1)),data(2),data(3),data(4)))}
//    else None
//  }}
//
//object readMultipleFileStreaming{
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setMaster("local[2]").setAppName("readLine")
//    val sc = new SparkContext(conf)
//    val ssc = new StreamingContext(sc,Seconds(30))
//    val lines = ssc.textFileStream("file:///home/hduser/Desktop/studentnew")
//    val line = lines.flatMap(student.func)
//    val filter = line.filter(_.marks > 100)
//    filter.print
////    println(filter.count)
//    filter.foreachRDD(rdd =>{
//      val uuid = UUID.randomUUID()
//      if(rdd.count()!=0) {
//        rdd.saveAsTextFile(s"file:///home/hduser/Desktop/studentnew_$uuid")}
//    })
//    ssc.start()
//    ssc.awaitTermination()
//
//  }}