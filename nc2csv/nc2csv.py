#! /usr/bin/env python

import argparse
import netCDF4 as nc
import os
import pandas as pd
import numpy as np

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
    write_nc_file(nc_file, csv_path, args)

def nc_path_to_csv_path(nc_path):
    csv_path = nc_path.removesuffix(".nc") + ".csv"
    return os.path.basename(csv_path)

def write_nc_file(nc_file, csv_path, args):
    nc_var = nc_file.variables[guess_primary_var(nc_file)]

    if not args.timeDimension in nc_var.dimensions:
        raise Exception("Time dimension '{}' did not exist in variable '{}'", args.timeDimension, args.primaryVar)

    time_dim, lat_dim, lng_dim = nc_var.get_dims()
    time_var = nc_file.variables[time_dim.name]
    times = nc.num2date(time_var[:], time_var.units)
    lats = nc_file.variables[lat_dim.name][:]
    lngs = nc_file.variables[lng_dim.name][:]

    time_grid, lat_grid, lng_grid = [
        x.flatten() for x in np.meshgrid(times, lats, lngs, indexing="ij")]

    df = pd.DataFrame({
        'time': [t.isoformat() for t in time_grid],
        'latitude': lat_grid,
        'longitude': lng_grid,
        nc_var.long_name: nc_var[:].flatten()
    })

    df.to_csv(cs_path)

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
    
