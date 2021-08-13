# druid-lookup

`druid-lookup` is an extension for Apache Druid. It adds the `geosieve` transform, which allows a ingestion task to map lat/long points to GISJOINs efficiently using filters created by the `geosieve-create` tool.

## Build

This is a Maven project that targets Java 8. Run
```
mvn clean package
```
to create a jar at `target/druid-geosieve-transform-0.21.0.jar`. 

To install, this jar should be moved to the `extensions/druid-geosieve-tansform` folder of your Druid installation, and then `common.runtime` edited to include `druid-geosieve-transform` in `druid.extensions.loadList`.

### Usage

First check to make sure the extension is loaded - this can be done on the web console in the Status window. You should see an entry for `sustain.geosieve.druid.geosievetransform.GeosieveTransformExtensionModule`, with an empty "Extension name" and "Version" field.

Currently, this extension only supports reading from a Redis database previously prepared by the `geosieve-create` tool. Therefore, you must have a Redis instance running somewhere.

The transform can be used by including a transform of type `geosieve` in the `transformSpec` of an ingestion spec. The format of a `geosieve` transform is as follows:

```json
{
    "type": "geosieve",
    "host": "< name of redis host >",
    "port": "< desired port >",
    "latProperty": "< name of the latitude property in the data set>",
    "lngProperty": "< name of the longitude property in the data set>",
    "name": "< name of the dimension to output GISJOINS to >",
    "prefix": "< the prefix used at filter creation - '_' if none was used >"
}
```
