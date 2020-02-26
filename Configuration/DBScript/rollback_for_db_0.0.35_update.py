import sys
import os
import csv

arguments_len = len(sys.argv) - 1
data_folder = '.'
if arguments_len is 1:
    data_folder = sys.argv[1]

print("parse the folder: %s" % data_folder)

def process_file(file_path):
    with open(file_path) as datas:
        data_reader = csv.reader(datas, delimiter='\t')
        for data in data_reader:
            if "gmail_scheduler_job" in file_path:
                print("UPDATE gmail_scheduler_job SET login_password = '%s' WHERE id = %s;" % (
                    data[2], data[0]))
            elif "scheduler_job" in file_path:
                print("UPDATE scheduler_job SET login_password = '%s' WHERE id = %s;" % (
                    data[2], data[0]))

for root, directories, files in os.walk(data_folder):
    for file in files:
        if "backup" in file:
            print("\n")
            process_file(os.path.join(root, file))
