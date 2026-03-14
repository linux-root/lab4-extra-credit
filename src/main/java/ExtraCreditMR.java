import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ExtraCreditMR {

	public static class MyMapper extends Mapper<LongWritable, Text, MyKey, Text> {
		private static final String VALID_QUALITY_CODES = "01459";

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			if (line.length() < 93)
				return;

			String yearStr = line.substring(15, 19);
			String stid = line.substring(3, 14);
			String airTempStr = line.substring(87, 92); // e.g. "+0203" or "-0111"
			String quality = line.substring(92, 93);

			int airTemp = Integer.parseInt(airTempStr);

			if (airTemp == 9999)
				return; // missing value
			if (!VALID_QUALITY_CODES.contains(quality))
				return; // bad quality

			MyKey myKey = new MyKey(stid, airTemp);
			context.write(myKey, new Text(yearStr));
		}
	}

	public static class MyReducer extends Reducer<MyKey, Text, MyKey, Text> {
		@Override
		protected void reduce(MyKey key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			for (Text value : values) {
				context.write(key, value);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "ExtraCredit");

		job.setJarByClass(ExtraCreditMR.class);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);

		job.setMapOutputKeyClass(MyKey.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(MyKey.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
