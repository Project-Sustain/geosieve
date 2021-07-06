#! /usr/bin/env python

import argparse

def main():
    try:
        args = parse_args()
        for nc_file in args['files']:
            print(nc_file)
    except Exception as e:
        print("Uncaught exception: " + str(e))

def parse_args():
    parser = argparse.ArgumentParser(description="Convert netCDF files to CSV format.")
    parser.add_argument("files", nargs="+")
    return parser.parse_args()

def nc_to_json(nc_file, args):
    

if __name__ == "__main__":
    main()

