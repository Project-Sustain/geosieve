# csv-combine

`csv-combine` is a MapReduce application for merging multiple CSV files with shared columns and one unique column into a set of CSV files that contain one of each column.

## Build

This is a Maven project that targets Java 8. Run
```
mvn clean package
```
to create a jar at `target/csv-combine-1.0-jar-with-dependencies.jar`.

As the name implies, this is a fairly hefty jar that contains _all_ dependencies, including Hadoop libraries.

## Run

Run on a Hadoop cluster with `hadoop jar`, specifying `sustain.combine.Main` as the main class:
```
$HADOOP_HOME/bin/hadoop jar csv-combine-1.0-jar-with-dependencies.jar 
```

### Options

* `-s <col1> <col2> ...`: Which columns are shared among all of the input CSV files. These columns will be output once in the resulting CSVs. They must be the first columns in all inputs CSVs. This is **required**.
* `-o <col1> <col2> ...`: A list of unique columns in the input CSVs. Each input CSV should contain each of the shared columns and then exactly one of these. They will be output once in the resulting CSVs **in sorted order**, not in the order they are provided here (!). This is **required**.
* `-p <regex>`: A regex that can be used to extract the name of a certain file's unique column from its filename. This is used by Mappers to determine what column they are reading, since they are not necessarily aware of the header. This is **required**.
* `-i <path>`: An HDFS path to a directory that contains the input CSVs. All files in this directory will be read, not just CSVs. This is **required**.
* `-n <count>`: How many nodes this program will run on. Defaults to the size of our HDFS cluster, which is 30 nodes. This is only used for optimizing the number of Reducers and should not cause incorrect execution if inaccurate.
