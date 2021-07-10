#! /usr/bin/env python

import argparse
import netCDF4
import os
import cProfile
import cftime
import threading
import concurrent.futures

args = None

def main():
    global args
    args = parse_args()
    if args.profile:
        cProfile.run('process_files(args.files)')
    else:
        process_files(args.files)

def parse_args():
    parser = argparse.ArgumentParser(description="Convert netCDF files to CSV format.")
    parser.add_argument("files", nargs="+")
    parser.add_argument("--timeDimension", "-t", default="time")
    parser.add_argument("--latDimension", "-a", default="lat")
    parser.add_argument("--lngDimension", "-n", default="lon")
    parser.add_argument("--crsDimension", "-c", default="crs")
    parser.add_argument("--primaryVar", "-v")
    parser.add_argument("--profile", "-p", action="store_true")
    parser.add_argument("--maxConcurrency", "-m", type=int, default=10)
    return parser.parse_args()

def process_files(files):
    with concurrent.futures.ThreadPoolExecutor(max_workers=args.maxConcurrency) as executor:
        executor.map(nc_to_csv, files)

def nc_to_csv(nc_path):
    nc_file = netCDF4.Dataset(nc_path, "r")
    csv_path = nc_path_to_csv_path(nc_path)
    write_nc_file(nc_file, csv_path)

def nc_path_to_csv_path(nc_path):
    csv_path = nc_path.removesuffix(".nc") + ".csv"
    return os.path.basename(csv_path)

def write_nc_file(nc_file, csv_path):
    primary_var = args.primaryVar if args.primaryVar else guess_primary_var(nc_file)
    nc_var = nc_file.variables[primary_var]

    check_dimensions(nc_var)

    with open(csv_path, "x", buffering=1048576) as csv_file:
        write_header(nc_file, nc_var, csv_file)
        write_to_csv(nc_file, nc_var, csv_file)

def write_header(nc_file, nc_var, csv_file):
    for dim in nc_var.dimensions:
        csv_file.write(str(dim) + ",")
    csv_file.write(nc_var.standard_name + "\n")

def write_to_csv(nc_file, nc_var, csv_file):
    write_dimension(nc_file, nc_var, nc_var[:].filled(), csv_file, 0, ());

def write_dimension(nc_file, nc_var, data, csv_file, current_dim_index, collected_values):
    if current_dim_index == len(nc_var.dimensions):
        # Don't write records for which there are missing values
        if -data == nc_var.missing_value:
            return

        for value in collected_values:
            csv_file.write(str(value) + ",")
        csv_file.write(str(data) + "\n")
    else:
        current_dim = nc_var.get_dims()[current_dim_index]
        for i in range(len(current_dim)):
            next_value = nc_file[current_dim.name][i]

            if current_dim.name == args.timeDimension:
                next_value = convert_to_iso(nc_file[current_dim.name], next_value)

            values = collected_values + (next_value,)
            write_dimension(nc_file, nc_var, data[i], csv_file, current_dim_index + 1, values)

def convert_to_iso(time_var, value):
    return cftime.num2date(value, time_var.units, time_var.calendar).isoformat()
    

def check_dimensions(nc_var):
    missing_dimensions = {
        'timeDimension': not args.timeDimension in nc_var.dimensions,
        'latDimension': not args.latDimension in nc_var.dimensions,
        'lngDimension': not args.lngDimension in nc_var.dimensions,
    }

    for dim in missing_dimensions.keys():
        if missing_dimensions[dim]:
            raise Exception("{} '{}' did not exist in variable {}!".format(dim, vars(args)[dim], nc_var.name))

def guess_primary_var(nc_file):
    common_vars = [args.timeDimension, args.latDimension, args.lngDimension, args.crsDimension]
    nc_vars = list(filter(lambda var: not var.lower() in common_vars, nc_file.variables))
    if len(nc_vars) == 0:
        raise Exception("Unable to find a variable other than {}.".format(common_vars))
    else:
        return nc_vars[0]

if __name__ == "__main__":
    main()
    
