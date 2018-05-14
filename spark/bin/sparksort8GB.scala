//load input file to the hdfs system and get the keys which are first 10 characters of the line
val start = System.currentTimeMillis() 
val file=sc.textFile("/input/data-8GB")
val file_sort=file.map(line => (line.take(10), line.drop(10)))
/*sort the keys and store the line as per the keys by concatenating the value and save the data in output file*/
val sortkey = file_sort.sortByKey()
val keyval=sortkey.map {case (key,value) => s"$key $value"}
var repartitioned = keyval.repartition(1)
repartitioned.saveAsTextFile("/user/bliang5/output-spark")
val end = System.currentTimeMillis()
println ("Sorting time:-" + (end - start) + "ms")