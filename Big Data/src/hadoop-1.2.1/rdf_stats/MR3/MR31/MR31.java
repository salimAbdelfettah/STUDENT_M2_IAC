//package org.myorg;
//http://localhost.localdomain:50060/logs/userlogs/
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class MR31 {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable zero = new IntWritable(0);
		private final static IntWritable one = new IntWritable(1);
		private final static IntWritable two = new IntWritable(2);

		private Text word = new Text();
		private Text s= new Text();
		private Text p= new Text();
		private Text o= new Text();
		private int vs=0;
		private int vo=0;

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			String line = value.toString();
			System.out.println(line);
			StringTokenizer tokenizer = new StringTokenizer(line);
			int i=0;
			while (tokenizer.hasMoreTokens()) {
				word.set(tokenizer.nextToken());
				if(word.equals(new Text("."))){
					if(p.toString().contains("rdf:type") || p.toString().contains("#type")){
						if(o.toString().contains("rdfs:Class") ||  o.toString().contains("#Class")){
							vs=-1;
							vo=-1;
						}else{
							vs=1;
							vo=-1;
						}
					}else if( p.toString().contains("rdfs:subClassOf") || p.toString().contains("#subClassOf")){
						vs=-1;
						vo=-1;
					}else if( p.toString().contains("rdfs:domain") || p.toString().contains("#domain") || p.toString().contains("rdfs:range") || p.toString().contains("#range")){
						vs=1;
						vo=-1;
					}else{
						vs=1;
						vo=1;
					}
					output.collect(s, new IntWritable(vs));
					output.collect(o, new IntWritable(vo));
					i=0;
				}else{
					switch (i){
						case 0: s.set(word); break;
						case 1: p.set(word); break;
						case 2: o.set(word); break;
					}
					i++;
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		private int sum, v;
		private boolean a=false;
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			sum=0;
			a=false;
			while (values.hasNext()) {
				v=values.next().get();
				if (!a&&v==-1){
					a=true;
				}
				sum++;
			}
			if(a){
				output.collect(key, new IntWritable(sum));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(MR31.class);
		conf.setJobName("MR31");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		conf.setOutputValueGroupingComparator(Comparator.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}

	public static class Comparator extends WritableComparator {
		private static final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();

		public Comparator() {
			super(Text.class);
		}


        	public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            		//try {
                		return (-1)* TEXT_COMPARATOR.compare(b1, s1, l1, b2, s2, l2);
            		//} catch (IOException e) {
                		//throw new IllegalArgumentException(e);
            		//}
        	}

        	public int compare(WritableComparable a, WritableComparable b) {
            		if (a instanceof Text && b instanceof Text) {
                		return (-1)*(((Text) a).compareTo((Text) b));
            		}
            		return super.compare(a, b);
        	}
	}

	static {
	// register this comparator
		WritableComparator.define(Text.class, new Comparator());
	}


}
