#! /usr/bin/env python

import argparse
import netCDF4 as nc
import os
import pandas as pd
import numpy as np
import cProfile

def main():
    args = parse_args()
    for nc_path in args.files:
        nc_to_csv(nc_path, args)

def parse_args():
    parser = argparse.ArgumentParser(description="Convert netCDF files to CSV format.")
    parser.add_argument("files", nargs="+")
    parser.add_argument("--timeDimension", "-t", default="time")
    parser.add_argument("--latDimension", "-a", default="lat")
    parser.add_argument("--lngDimension", "-n", default="lon")
    return parser.parse_args()

def nc_to_csv(nc_path, args):
    nc_file = nc.Dataset(nc_path, "r")
    csv_path = nc_path_to_csv_path(nc_path)
    write_nc_file(nc_file, csv_path, args)

def nc_path_to_csv_path(nc_path):
    csv_path = nc_path.removesuffix(".nc") + ".csv"
    return os.path.basename(csv_path)

def write_nc_file(nc_file, csv_path, args):
    nc_var = nc_file.variables[guess_primary_var(nc_file, args)]

    check_dimensions(nc_var, args)

    write_with_pandas(nc_var, nc_file, csv_path, args)

def write_with_pandas(nc_var, nc_file, csv_path, args):
    time_var = nc_file.variables[args.timeDimension]
    times = nc.num2date(time_var[:], time_var.units)
    lats = nc_file.variables[args.latDimension][:]
    lngs = nc_file.variables[args.lngDimension][:]

    time_grid, lat_grid, lng_grid = [
        x.flatten() for x in np.meshgrid(times, lats, lngs, indexing="ij")]

    df = pd.DataFrame({
        'time': [t.isoformat() for t in time_grid],
        'latitude': lat_grid,
        'longitude': lng_grid,
        nc_var.long_name: nc_var[:].flatten()
    })

    df.to_csv(csv_path)

def check_dimensions(nc_var, args):
    missing_dimensions = {
        'timeDimension': not args.timeDimension in nc_var.dimensions,
        'latDimension': not args.latDimension in nc_var.dimensions,
        'lngDimension': not args.lngDimension in nc_var.dimensions,
    }

    for dim in missing_dimensions.keys():
        if missing_dimensions[dim]:
            raise Exception("{} '{}' did not exist in variable {}!".format(dim, vars(args)[dim], nc_var.name))

def guess_primary_var(nc_file, args):
    common_vars = [args.timeDimension, args.latDimension, args.lngDimension, 'crs']
    nc_vars = list(filter(lambda var: not var.lower() in common_vars, nc_file.variables))
    if len(nc_vars) == 0:
        raise Exception("Unable to find a variable other than {}.".format(common_vars))
    else:
        return nc_vars[0]

if __name__ == "__main__":
    cProfile.run('main()')
    
