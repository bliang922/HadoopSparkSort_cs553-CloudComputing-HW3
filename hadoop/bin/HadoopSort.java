
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.io.compress.SnappyCodec;
public class HadoopSort {
	
	public static class HadoopMapper extends Mapper<Object,Text,Text,Text> {

	       public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
		  //
	    	  String stringkey=value.toString().substring(0,10);
	          String stringvalue=value.toString().substring(10);
	          context.write(new Text(stringkey),new Text(stringvalue));
      
	       }
	    }            
	    public static class HadoopReducer extends Reducer<Text, Text, Text, Text> {
	   
	       public void reduce(Text key, Iterable<Text> values, Context context) 
	         throws IOException, InterruptedException {
			for(Text value:values)
	               context.write(key,value);
	       }
	    }

	    public static void main(String[] args) throws Exception {
	        Configuration conf = new Configuration();
		//turn on intermediate (map output) compression
		conf.set("mapreduce.map.output.compress", "true");
	//	conf.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.SnappyCodec");      
		long start = System.currentTimeMillis();
	//	mapreduce.map.output.compress=true;
	//	mapreduce.map.ouput.compress.codec=org.apache.hadoop.io.compress.*;        
	        Job job = Job.getInstance(conf,"teraSort");
	  //      job.setNumReduceTasks(8);
	        job.setJarByClass(HadoopSort.class);
	        job.setMapOutputKeyClass(Text.class);
	        job.setMapOutputValueClass(Text.class);
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(Text.class);		  
	        job.setMapperClass(HadoopMapper.class);
	        job.setReducerClass(HadoopReducer.class);
	        job.setInputFormatClass(TextInputFormat.class);
	        job.setOutputFormatClass(TextOutputFormat.class);
			job.setNumReduceTasks(8);    
	        FileInputFormat.addInputPath(job, new Path(args[0]));
	        FileOutputFormat.setOutputPath(job, new Path(args[1]));
	            
	        job.waitForCompletion(true);
	        
	        
	        long end = System.currentTimeMillis();
	        double timediff = (end - start)/1000;
		System.out.println("Total Time=    " + timediff +" seconds");
	     }

}
