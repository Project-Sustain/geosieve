## geosieve-create

`geosieve-create` is a utility for creating bloom filters to more efficiently map lat/long points to GISJOINs. 

### Build

This is a Maven project that targets Java 8. Run
```
mvn clean package
```
to create an executable jar at `target/create-1.0-jar-with-dependencies.jar`.

Any Java version newer than 8 will probably work since the tool is not extremely complicated, but no guarantees.

### Usage

Run with `java -jar`, as in
```
java -jar create-1.0-jar-with-dependencies.jar -h
```

The `-h` flag will print a usage screen. Running with no arguments will launch the program with complete default configurations, which is likely not what you want, so be careful.

#### Options

The `create` tool essentially needs to know three things: where to find the lat/long points, how to transform lat/long points into GISJOINs, and where to put the resulting filters. The following options for each are currently supported:

##### Point source

Determines where to get lat/long points. Controlled by the `-s` flag, which can be either `UNIFORM` or `FILE`. Defaults to `UNIFORM`.

* `-s UNIFORM`: This will create lat/long points on a uniform grid with a given set of bounds and precision. The options available when using this source are:
  * `-g <precision>`: To what decimal precision the grid points should be. For instance, at 1 precision, points will step like 0.0, 0.1, 0.2, 0.3, etc. while at 2 precision, they will step like 0.00, 0.01, 0.02, etc. Defaults to 1.
  * `-e <north> <east> <south> <west>`: Specifies a rectangular bound for the grid in decimal degrees. For example, the contiguous US can be represented by 41.34, -66.89, 24.40, -124.85. Defaults to a bounding box over Colorado.
    * **NOTE** Because the argument parsing library this program uses Sucks:tm:, it will incorrectly interpret negative numbers as option arguments. You can get around this by quoting negative numbers and adding a space at the very beginning, i.e. `" -66.89"` instead of `-66.89`.
* `-s FILE`: This will read lat/long points from a file. The only currently supported file types are JSON and CSV. For JSON, either a valid JSON document or one valid JSON object per line is supported. The options available when using this source are:
  * `-a <field name>`: What the name of the latitude property or field is in the input file. Defaults to "latitude".
  * `-n <field name>`: What the name of the longitude property or field is in the input file. Defaults to "longitude".
  * `-f <file1> <file2> ...`: The files to read points from. The type of file is automatically determined by its extension, meaning the extension must be accurate. Defaults to "data.json".

##### GISJOIN source

Determines how lat/long points are mapped to GISJOINs. Controlled by the `-j` flag. The only supported option at the moment is `SUSTAIN_MONGO`, which maps lat/lon points by sending `geoIntersects` requests to our MongoDB instance. This has the implication that you must be on the CSU vpn or network.

There are no sub-options.

##### Filter database

Determines where the resulting filters and other necessary metadata are output. Controlled by the `-t` flag, which can be either `REDIS` or `FILE`.

* `-t REDIS`: This will output filters into a Redis instance that has the Rebloom module loaded. It will connect only over `localhost` with port `6379`, so this must be run on the exact machine that contains the Redis server and Redis must be listening on the default port 6379. This options available when using this source are:
  * `-p <prefix>`: A string with which to prefix all keys added to the Redis database. This helps separate keys created by different runs of the tool or from different datasets. Defaults to a single underscore, `_`.
* `-t FILE`: This is intended for testing or debugging purposes and is not necessarily feature complete or efficient. The options available when using this source are:
  * `-o <file>`: The file to output to, which will be created if it does not exist. Defaults to `out.txt`.

##### General options

These options control general behavior and are independent of all other options.

* `-c <amount>`: The number of threads to launch for processing. Note that too many threads with the `UNIFORM` point source may cause issues if the grid extents are small.