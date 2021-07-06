#! /usr/bin/env python

import argparse
import netCDF4 as nc
import os

def main():
    args = parse_args()
    for nc_path in args.files:
        nc_to_csv(nc_path, args)

def parse_args():
    parser = argparse.ArgumentParser(description="Convert netCDF files to CSV format.")
    parser.add_argument("files", nargs="+")
    parser.add_argument("--timeDimension", "-t", default="time")
    return parser.parse_args()

def nc_to_csv(nc_path, args):
    nc_file = nc.Dataset(nc_path, "r")
    csv_path = nc_path_to_csv_path(nc_path)

    with open(csv_path, "r") as csv_file:
        write_nc_file(nc_file, csv_file, args)

def nc_path_to_csv_path(nc_path):
    csv_path = nc_path.removesuffix(".nc") + ".csv"
    return os.path.basename(csv_path)

def write_nc_file(nc_file, csv_file, args):
    nc_var = nc_file.variables[guess_primary_var(nc_file)]

    if not args.timeDimension in nc_var.dimensions:
        raise Exception("Time dimension '{}' did not exist in variable '{}'", args.timeDimension, args.primaryVar)

    write_nc_header(nc_var, csv_file)
    write_nc_var(nc_file, nc_var, csv_file)

def write_nc_header(nc_var, csv_file):
    for dim in nc_var.dimensions:
        csv_file.write(dim + ",")
    csv_file.write(nc_var.long_name)

def write_nc_var(nc_file, nc_var, csv_file):
    write_dimensions(nc_file, nc_var, csv_file, nc_var.get_dims(), ())

def write_dimensions(nc_file, nc_var, csv_file, remaining_dims, collected_dims):
    if len(remaining_dims) == 0:
        write_values(nc_var, csv_file, collected_dims)
    else:
        dim = remaining_dims[0]
        for i in len(dim):
            dim_metadata = { 'value': nc_file.variables[dim.name][i], "index": i }
            write_dimensions(nc_var, csv_file, remaining_dims[1:], collected_dims + (dim_metadata,))

def write_values(nc_var, csv_file, dims):
    data = None
    for dim in dims:
        csv_file.write(dim['value'] + ",")
        data = nc_var[dim['index']]
    csv_file.write(data)

def guess_primary_var(nc_file):
    common_vars = [
        'lon', 'long', 'longitude', 'lng',
        'lat', 'lati', 'latitude',
        'time', 'timestamp',
        'crs'
    ]
    
    nc_vars = list(filter(lambda var: not var.lower() in common_vars, nc_file.variables))
    if len(nc_vars) == 0:
        raise Exception("Unable to find a primary variable.")
    else:
        return nc_vars[0]


if __name__ == "__main__":
    main()
    
